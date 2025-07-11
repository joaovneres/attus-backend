package br.com.attus.processos.event.kafka;

import br.com.attus.processos.nucleo.application.port.out.acao.AcaoCommandPublisherPort;
import br.com.attus.processos.nucleo.dominio.event.AcaoCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcaoCommandKafkaPublisher implements AcaoCommandPublisherPort {

    private static final String TOPIC = "acao.commands";

    private final KafkaTemplate<String, Object> kafka;

    @Override
    public void publish(AcaoCommand cmd) {
        kafka.send(TOPIC, cmd)
                .whenComplete((res, ex) -> {
                    if (ex != null) {
                        log.error("Falha ao publicar comando {}", cmd, ex);
                    } else if (res != null && res.getRecordMetadata() != null) {
                        RecordMetadata m = res.getRecordMetadata();
                        log.debug("🡅 AcaoCommand {} publicado offset={} partition={}",
                                cmd.getClass().getSimpleName(),
                                m.offset(), m.partition());
                    } else {
                        log.warn("Resposta nula ao publicar comando {}", cmd);
                    }
                });
    }
}
