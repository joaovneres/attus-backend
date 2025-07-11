package br.com.attus.processos.nucleo.application.port.out.parte;

import br.com.attus.processos.nucleo.dominio.event.ParteCommand;

public interface ParteCommandPublisherPort {
    void publish(ParteCommand cmd);
}
