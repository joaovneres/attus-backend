package br.com.attus.processos.nucleo.dominio.event;

import br.com.attus.processos.nucleo.dominio.enums.TipoParte;

/** Commands da entidade Parte. */
public sealed interface ParteCommand permits ParteCommand.Create, ParteCommand.UpdateContato {

    record Create(Long processoId,
                  String nome,
                  String cpfCnpj,
                  TipoParte tipo,
                  String email,
                  String telefone) implements ParteCommand { }

    record UpdateContato(Long parteId,
                         String email,
                         String telefone) implements ParteCommand { }
}
