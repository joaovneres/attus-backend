package br.com.attus.processos.nucleo.dominio.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Embeddable
@EqualsAndHashCode(of = "valor")
public class DocumentoFiscal {

    private String valor;

    protected DocumentoFiscal() {}

    public DocumentoFiscal(String valor) {
        if (!isCpf(valor) && !isCnpj(valor)) {
            throw new IllegalArgumentException("CPF ou CNPJ inválido: " + valor);
        }
        this.valor = valor;
    }

    private boolean isCpf(String v) { return v != null && v.matches("\\d{11}"); }
    private boolean isCnpj(String v) { return v != null && v.matches("\\d{14}"); }

    @Override public String toString() { return valor; }
}
