package br.com.attus.processos.nucleo.dominio.event;

import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;

import java.time.LocalDate;
import java.util.Objects;

public sealed interface AcaoCommand permits AcaoCommand.Create {

    record Create(Long processoId,
                  TipoAcao tipo,
                  LocalDate dataRegistro,
                  String descricao) implements AcaoCommand {
        public Create {
            Objects.requireNonNull(processoId, "processoId não pode ser nulo");
            if (processoId <= 0) {
                throw new IllegalArgumentException("processoId deve ser > 0");
            }
            Objects.requireNonNull(tipo, "tipo não pode ser nulo");
            Objects.requireNonNull(dataRegistro, "dataRegistro não pode ser nulo");
        }
    }
}
