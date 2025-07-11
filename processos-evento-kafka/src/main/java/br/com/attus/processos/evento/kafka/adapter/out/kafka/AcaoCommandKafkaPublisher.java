package br.com.attus.processos.evento.kafka.adapter.out.kafka;

import br.com.attus.processos.nucleo.application.port.out.acao.AcaoCommandPublisherPort;
import br.com.attus.processos.nucleo.dominio.event.AcaoCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AcaoCommandKafkaPublisher implements AcaoCommandPublisherPort {

    private final KafkaTemplate<String, AcaoCommand> kafka;
    private static final String TOPIC = "acao.commands";

    @Override
    public void publish(AcaoCommand cmd) {
        kafka.send(TOPIC, cmd);
    }
}
