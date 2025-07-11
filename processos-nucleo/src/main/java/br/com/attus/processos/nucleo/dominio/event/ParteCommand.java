package br.com.attus.processos.nucleo.dominio.event;

import br.com.attus.processos.nucleo.dominio.enums.TipoParte;

import java.util.Objects;

public sealed interface ParteCommand permits ParteCommand.Create, ParteCommand.UpdateContato {

    record Create(Long processoId,
                  String nome,
                  String cpfCnpj,
                  TipoParte tipo,
                  String email,
                  String telefone) implements ParteCommand {
        public Create {
            Objects.requireNonNull(processoId, "processoId não pode ser nulo");
            if (processoId <= 0) {
                throw new IllegalArgumentException("processoId deve ser > 0");
            }
            Objects.requireNonNull(nome, "nome não pode ser nulo");
            if (nome.isBlank()) {
                throw new IllegalArgumentException("nome não pode ser vazio");
            }
            Objects.requireNonNull(cpfCnpj, "cpfCnpj não pode ser nulo");
            if (cpfCnpj.isBlank()) {
                throw new IllegalArgumentException("cpfCnpj não pode ser vazio");
            }
            Objects.requireNonNull(tipo, "tipo não pode ser nulo");
        }
    }

    record UpdateContato(Long parteId,
                         String email,
                         String telefone) implements ParteCommand {
        public UpdateContato {
            Objects.requireNonNull(parteId, "parteId não pode ser nulo");
            if (parteId <= 0) {
                throw new IllegalArgumentException("parteId deve ser > 0");
            }
        }
    }
}
