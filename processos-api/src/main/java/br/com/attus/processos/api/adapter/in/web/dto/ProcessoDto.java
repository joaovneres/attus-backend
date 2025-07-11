package br.com.attus.processos.api.adapter.in.web.dto;

import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public interface ProcessoDto {

    record CreateRequest(
            @NotBlank String numero,
            @NotNull  LocalDate dataAbertura,
            String descricao
    ) { }

    record UpdateRequest(
            String descricao
    ) { }

    record Response(
            Long id,
            String numero,
            LocalDate dataAbertura,
            String descricao,
            StatusProcesso status,
            LocalDate criadoEm,
            LocalDate atualizadoEm
    ) { }
}