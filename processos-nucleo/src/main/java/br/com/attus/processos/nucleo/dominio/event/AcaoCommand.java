package br.com.attus.processos.nucleo.dominio.event;

import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import java.time.LocalDate;

/** Commands relacionados à entidade Ação. */
public sealed interface AcaoCommand permits AcaoCommand.Create {

    record Create(Long processoId,
                  TipoAcao tipo,
                  LocalDate dataRegistro,
                  String descricao) implements AcaoCommand { }
}
