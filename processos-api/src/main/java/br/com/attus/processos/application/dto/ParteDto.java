package br.com.attus.processos.application.dto;

import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public interface ParteDto {

    record CreateRequest(

            @NotNull(message = "processoId é obrigatório")
            Long processoId,

            @NotBlank(message = "nome é obrigatório")
            @Size(max = 255, message = "nome deve ter até 255 caracteres")
            String nome,

            @NotBlank(message = "cpfCnpj é obrigatório")
            @Pattern(
                    regexp = "(\\d{11})|(\\d{14})",
                    message = "cpfCnpj deve conter 11 ou 14 dígitos")
            String cpfCnpj,

            @NotNull(message = "tipo é obrigatório")
            TipoParte tipo,

            @Email(message = "email inválido")
            String email,

            @Size(max = 30, message = "telefone deve ter até 30 caracteres")
            String telefone

    ) implements Serializable {
    }

    record UpdateContatoRequest(
            @Email(message = "email inválido")
            String email,

            @Size(max = 30, message = "telefone deve ter até 30 caracteres")
            String telefone
    ) implements Serializable {
    }

    record Response(
            Long id,
            Long processoId,
            String nome,
            String cpfCnpj,
            TipoParte tipo,
            String email,
            String telefone
    ) implements Serializable {
    }
}
