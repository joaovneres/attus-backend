package br.com.attus.processos.nucleo.dominio.event;

import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;

import java.time.LocalDate;
import java.util.Objects;

public sealed interface ProcessoCommand
        permits ProcessoCommand.Create,
        ProcessoCommand.UpdateDesc,
        ProcessoCommand.ChangeStatus {

    record Create(String numero,
                  LocalDate dataAbertura,
                  String descricao) implements ProcessoCommand {
        public Create {
            Objects.requireNonNull(numero, "numero não pode ser nulo");
            if (numero.isBlank()) {
                throw new IllegalArgumentException("numero não pode ser vazio");
            }
            Objects.requireNonNull(dataAbertura, "dataAbertura não pode ser nula");
            if (dataAbertura.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("dataAbertura não pode estar no futuro");
            }
        }
    }

    record UpdateDesc(Long id,
                      String descricao) implements ProcessoCommand {
        public UpdateDesc {
            Objects.requireNonNull(id, "id não pode ser nulo");
            if (id <= 0) {
                throw new IllegalArgumentException("id deve ser > 0");
            }
        }
    }

    record ChangeStatus(Long id,
                        StatusProcesso novoStatus) implements ProcessoCommand {
        public ChangeStatus {
            Objects.requireNonNull(id, "id não pode ser nulo");
            if (id <= 0) {
                throw new IllegalArgumentException("id deve ser > 0");
            }
            Objects.requireNonNull(novoStatus, "novoStatus não pode ser nulo");
        }
    }
}