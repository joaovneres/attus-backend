package br.com.attus.processos.consumer.kafka;

import br.com.attus.processos.consumer.persistence.ProcessoRepository;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import br.com.attus.processos.nucleo.dominio.event.ProcessoCommand;
import jakarta.transaction.Transactional;

import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessoCommandListener {

    private static final String TOPIC = "processo.commands";
    private static final String GROUP = "processo-consumidor";

    private final ProcessoRepository repo;

    @KafkaListener(
            topics = TOPIC,
            groupId = GROUP,
            containerFactory = "kafkaJsonConcurrentFactory")
    @Transactional
    public void onCommand(@Payload ProcessoCommand cmd,
                          ConsumerRecord<?, ?> rec) {

        log.debug("⇢ cmd proc offset={} key={} {}", rec.offset(), rec.key(), cmd);

        if (cmd instanceof ProcessoCommand.Create c) {
            handleCreate(c);
        } else if (cmd instanceof ProcessoCommand.UpdateDesc u) {
            handleUpdateDesc(u);
        } else if (cmd instanceof ProcessoCommand.ChangeStatus s) {
            handleChangeStatus(s);
        } else {
            log.warn("Tipo de comando Processo não suportado: {}", cmd.getClass().getName());
        }
    }

    private void handleCreate(ProcessoCommand.Create c) {
        repo.findByNumero(c.numero()).ifPresent(p -> {
            throw new IllegalStateException("Processo já existe: " + c.numero());
        });

        Processo novo = new Processo(c.numero(), c.dataAbertura(), c.descricao());
        repo.save(novo);

        log.info("✓ Processo criado id={} numero={}", novo.getId(), novo.getNumero());
    }

    private void handleUpdateDesc(ProcessoCommand.UpdateDesc u) {
        Processo p = repo.findById(u.id())
                .orElseThrow(() -> new NoSuchElementException("Processo não encontrado: " + u.id()));

        p.atualizarDescricao(u.descricao());
        repo.flush();

        log.info("✓ Descrição atualizada processo id={}", p.getId());
    }

    private void handleChangeStatus(ProcessoCommand.ChangeStatus s) {
        Processo p = repo.findById(s.id())
                .orElseThrow(() -> new NoSuchElementException("Processo não encontrado: " + s.id()));

        StatusProcesso novo = s.novoStatus();
        switch (novo) {
            case ARQUIVADO -> p.arquivar();
            case SUSPENSO -> p.suspender();
            case ATIVO -> p.reabrir();
            default -> throw new IllegalStateException("Status desconhecido: " + novo);
        }

        repo.flush();
        log.info("✓ Processo id={} mudou status para {}", p.getId(), novo);
    }
}
