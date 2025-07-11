package br.com.attus;

import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.event.ProcessoCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProcessCommandListener {

    private final ProcessoRepositoryPort repo;

    @KafkaListener(topics = "processo.commands", groupId = "processo")
    @Transactional
    public void onCommand(ProcessoCommand cmd) {
        switch (cmd) {

            case ProcessoCommand.Create c -> handleCreate(c);
            case ProcessoCommand.UpdateDesc u -> handleUpdate(u);
            case ProcessoCommand.ChangeStatus s -> handleStatus(s);
        }
    }
    /* métodos privados salvando no DB */
}
