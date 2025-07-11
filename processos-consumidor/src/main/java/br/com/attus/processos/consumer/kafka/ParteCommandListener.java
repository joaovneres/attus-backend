package br.com.attus.processos.consumer.kafka;

import br.com.attus.processos.consumer.persistence.ParteRepository;
import br.com.attus.processos.consumer.persistence.ProcessoRepository;
import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.event.ParteCommand;
import br.com.attus.processos.nucleo.dominio.vo.Contato;
import br.com.attus.processos.nucleo.dominio.vo.DocumentoFiscal;
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
public class ParteCommandListener {

    private static final String GRUPO = "processo-consumidor";

    private final ParteRepository     parteRepo;
    private final ProcessoRepository  procRepo;

    @Transactional
    @KafkaListener(
            topics          = "parte.commands",
            groupId         = GRUPO,
            containerFactory = "kafkaJsonConcurrentFactory")
    public void onCommand(@Payload ParteCommand cmd,
                          ConsumerRecord<?, ?> record) {

        log.debug("Comando Parte offset={} key={} tipo={}",
                record.offset(), record.key(), cmd.getClass().getSimpleName());

        if (cmd instanceof ParteCommand.Create c)          handleCreate(c);
        else if (cmd instanceof ParteCommand.UpdateContato u) handleUpdateContato(u);
        else log.warn("Tipo de comando Parte não suportado: {}", cmd.getClass().getName());
    }

    private void handleCreate(ParteCommand.Create c) {
        Processo proc = procRepo.findById(c.processoId())
                .orElseThrow(() -> new NoSuchElementException("Processo não encontrado: " + c.processoId()));

        Parte parte = new Parte(
                proc,
                c.nome(),
                new DocumentoFiscal(c.cpfCnpj()),
                c.tipo(),
                toContatoOrNull(c.email(), c.telefone()));

        parteRepo.saveAndFlush(parte);

        log.info("Parte criada id={} processo={} tipo={}",
                parte.getId(), proc.getId(), c.tipo());
    }

    private void handleUpdateContato(ParteCommand.UpdateContato u) {
        Parte parte = parteRepo.findById(u.parteId())
                .orElseThrow(() -> new NoSuchElementException("Parte não encontrada: " + u.parteId()));

        parte.atualizarContato(toContatoOrNull(u.email(), u.telefone()));
        parteRepo.flush();

        log.info("Contato atualizado para parte id={}", parte.getId());
    }

    private Contato toContatoOrNull(String email, String tel) {
        return (email == null && tel == null) ? null : new Contato(email, tel);
    }
}
