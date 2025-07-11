package br.com.attus.processos.application.dto;

import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

public interface AcaoDto {

    record CreateRequest(
            @NotNull(message = "processoId é obrigatório") Long processoId,
            @NotNull(message = "tipo é obrigatório")       TipoAcao tipo,
            @NotNull(message = "dataRegistro é obrigatória") LocalDate dataRegistro,
            @Size(max = 1_000)                              String descricao
    ) implements Serializable { }

    record Response(
            Long id,
            Long processoId,
            TipoAcao tipo,
            LocalDate dataRegistro,
            String descricao
    ) implements Serializable { }
}
