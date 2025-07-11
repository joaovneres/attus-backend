/* ParteDto.java */
package br.com.attus.processos.api.adapter.in.web.dto;

import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface ParteDto {

    /* POST /processos/{id}/partes */
    record CreateRequest(
            @NotNull Long   processoId,
            @NotBlank String nome,
            @NotBlank String cpfCnpj,
            @NotNull TipoParte tipo,
            String email,
            String telefone
    ) { }

    /* PATCH /partes/{id}/contato */
    record UpdateContatoRequest(
            String email,
            String telefone
    ) { }

    /* Response */
    record Response(
            Long id,
            Long processoId,
            String nome,
            String cpfCnpj,
            TipoParte tipo,
            String email,
            String telefone
    ) { }
}
