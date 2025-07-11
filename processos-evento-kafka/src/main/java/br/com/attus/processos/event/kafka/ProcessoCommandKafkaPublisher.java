package br.com.attus.processos.event.kafka;

import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoCommandPublisherPort;
import br.com.attus.processos.nucleo.dominio.event.ProcessoCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessoCommandKafkaPublisher implements ProcessoCommandPublisherPort {

    private static final String TOPIC = "processo.commands";

    private final KafkaTemplate<String, Object> kafka;

    @Override
    public void publish(ProcessoCommand cmd) {
        kafka.send(TOPIC, cmd)
                .whenComplete((r, ex) -> {
                    if (ex != null) {
                        log.error("Falha ao publicar comando {}", cmd, ex);
                    } else if (r != null && r.getRecordMetadata() != null) {
                        RecordMetadata m = r.getRecordMetadata();
                        log.debug("🡅 ProcessoCommand {} publicado offset={} partition={}",
                                cmd.getClass().getSimpleName(),
                                m.offset(), m.partition());
                    } else {
                        log.warn("Resposta nula ao publicar comando {}", cmd);
                    }
                });
    }
}
