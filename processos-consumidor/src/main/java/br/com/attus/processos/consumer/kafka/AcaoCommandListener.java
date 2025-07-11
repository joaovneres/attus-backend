package br.com.attus.processos.consumer.kafka;

import br.com.attus.processos.consumer.persistence.AcaoRepository;
import br.com.attus.processos.consumer.persistence.ProcessoRepository;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.event.AcaoCommand;
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
public class AcaoCommandListener {

    private static final String GRUPO = "processo-consumidor";

    private final AcaoRepository      acaoRepo;
    private final ProcessoRepository  procRepo;

    @Transactional
    @KafkaListener(
            topics   = "acao.commands",
            groupId  = GRUPO,
            containerFactory = "kafkaJsonConcurrentFactory")
    public void onCommand(@Payload AcaoCommand cmd,
                          ConsumerRecord<?, ?> record) {

        log.debug("Comando recebido offset={} key={} tipo={}",
                record.offset(), record.key(), cmd.getClass().getSimpleName());

        if (cmd instanceof AcaoCommand.Create create) {
            handleCreate(create);
        } else {
            log.warn("Tipo de comando não suportado: {}", cmd.getClass().getName());
        }
    }

    private void handleCreate(AcaoCommand.Create c) {
        Processo proc = procRepo.findById(c.processoId())
                .orElseThrow(() -> new NoSuchElementException("Processo não encontrado: " + c.processoId()));

        Acao nova = new Acao(proc, c.tipo(), c.dataRegistro(), c.descricao());
        acaoRepo.saveAndFlush(nova);

        log.info("Ação criada id={} processo={} tipo={}",
                nova.getId(), proc.getId(), nova.getTipo());
    }
}
