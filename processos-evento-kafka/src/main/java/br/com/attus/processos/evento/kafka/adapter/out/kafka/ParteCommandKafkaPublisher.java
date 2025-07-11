package br.com.attus.processos.evento.kafka.adapter.out.kafka;

import br.com.attus.processos.nucleo.application.port.out.parte.ParteCommandPublisherPort;
import br.com.attus.processos.nucleo.dominio.event.ParteCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ParteCommandKafkaPublisher implements ParteCommandPublisherPort {

    private final KafkaTemplate<String, ParteCommand> kafka;
    private static final String TOPIC = "parte.commands";

    @Override
    public void publish(ParteCommand cmd) {
        kafka.send(TOPIC, cmd);
    }
}
