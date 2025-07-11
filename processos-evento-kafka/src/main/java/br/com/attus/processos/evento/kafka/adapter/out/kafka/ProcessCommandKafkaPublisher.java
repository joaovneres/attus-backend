package br.com.attus.processos.evento.kafka.adapter.out.kafka;

import br.com.attus.processos.nucleo.application.port.out.ProcessoCommandPublisherPort;
import br.com.attus.processos.nucleo.dominio.event.ProcessoCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProcessCommandKafkaPublisher implements ProcessoCommandPublisherPort {

    private final KafkaTemplate<String, ProcessoCommand> kafka;
    private static final String TOPIC = "processo.commands";

    @Override
    public void publish(ProcessoCommand cmd) {
        kafka.send(TOPIC, cmd);
    }
}
