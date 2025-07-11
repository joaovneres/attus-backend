package br.com.attus.processos.nucleo.application.port.out.acao;

import br.com.attus.processos.nucleo.dominio.event.AcaoCommand;

public interface AcaoCommandPublisherPort {
    void publish(AcaoCommand cmd);
}
