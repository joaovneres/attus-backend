package br.com.attus.processos.nucleo.application.port.out.processo;

import br.com.attus.processos.nucleo.dominio.event.ProcessoCommand;

public interface ProcessoCommandPublisherPort {

    void publish(ProcessoCommand cmd);
}
