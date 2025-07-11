package br.com.attus.processos.nucleo.dominio.event;

import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import java.time.LocalDate;

public sealed interface ProcessoCommand permits
        ProcessoCommand.Create, ProcessoCommand.UpdateDesc, ProcessoCommand.ChangeStatus {

    record Create(String numero,
                  LocalDate dataAbertura,
                  String descricao) implements ProcessoCommand { }

    record UpdateDesc(Long id,
                      String descricao) implements ProcessoCommand { }

    record ChangeStatus(Long id,
                        StatusProcesso novoStatus) implements ProcessoCommand { }
}
