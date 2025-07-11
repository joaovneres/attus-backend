package br.com.attus.processos.nucleo.dominio.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode(of = "valor")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentoFiscal {

    @Column(name = "cpf_cnpj", length = 14, nullable = false)
    private String valor;

    public DocumentoFiscal(String valor) {
        Objects.requireNonNull(valor, "valor não pode ser nulo");
        if (!valor.matches("\\d{11}") && !valor.matches("\\d{14}")) {
            throw new IllegalArgumentException("CPF ou CNPJ inválido: " + valor);
        }
        this.valor = valor;
    }

    @Override
    public String toString() {
        return valor;
    }
}
