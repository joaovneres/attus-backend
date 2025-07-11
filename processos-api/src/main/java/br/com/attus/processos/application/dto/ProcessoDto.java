package br.com.attus.processos.application.dto;

import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

public interface ProcessoDto {

    record CreateRequest(

            @NotBlank(message = "numero é obrigatório")
            @Size(max = 30, message = "numero deve ter até 30 caracteres")
            String numero,

            @NotNull(message = "dataAbertura é obrigatória")
            @PastOrPresent(message = "dataAbertura não pode estar no futuro")
            LocalDate dataAbertura,

            @Size(max = 1_000, message = "descricao deve ter até 1 000 caracteres")
            String descricao

    ) implements Serializable {
    }

    record UpdateRequest(
            @Size(max = 1_000, message = "descricao deve ter até 1 000 caracteres")
            String descricao
    ) implements Serializable {
    }

    record Response(
            Long id,
            String numero,
            LocalDate dataAbertura,
            String descricao,
            StatusProcesso status,
            Instant criadoEm,
            Instant atualizadoEm
    ) implements Serializable {
    }
}
