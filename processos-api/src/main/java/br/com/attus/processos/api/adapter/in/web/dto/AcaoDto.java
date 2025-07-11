/* AcaoDto.java */
package br.com.attus.processos.api.adapter.in.web.dto;

import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public interface AcaoDto {

    record CreateRequest(
            @NotNull Long processoId,
            @NotNull TipoAcao tipo,
            @NotNull LocalDate dataRegistro,
            String descricao
    ) { }

    record Response(
            Long id,
            Long processoId,
            TipoAcao tipo,
            LocalDate dataRegistro,
            String descricao
    ) { }
}
