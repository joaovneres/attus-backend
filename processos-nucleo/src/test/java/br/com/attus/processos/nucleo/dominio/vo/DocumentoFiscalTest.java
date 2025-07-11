/* src/test/java/br/com/attus/processos/nucleo/dominio/vo/DocumentoFiscalTest.java */
package br.com.attus.processos.nucleo.dominio.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DocumentoFiscalTest {

    @Test
    @DisplayName("Deve aceitar CPF válido (11 dígitos)")
    void cpfValido() {
        DocumentoFiscal doc = new DocumentoFiscal("12345678901");
        assertThat(doc.getValor()).isEqualTo("12345678901");
    }

    @Test
    @DisplayName("Deve aceitar CNPJ válido (14 dígitos)")
    void cnpjValido() {
        DocumentoFiscal doc = new DocumentoFiscal("12345678000199");
        assertThat(doc.getValor()).isEqualTo("12345678000199");
    }

    @Test
    @DisplayName("Deve lançar NullPointerException para valor nulo")
    void deveFalharQuandoNulo() {
        assertThatThrownBy(() -> new DocumentoFiscal(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("valor não pode ser nulo");
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para formato inválido")
    void deveFalharFormatoInvalido() {
        assertThatThrownBy(() -> new DocumentoFiscal("123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CPF ou CNPJ inválido");
    }

    @Test
    @DisplayName("equals/hashCode considera apenas o valor")
    void equalsHashCode() {
        DocumentoFiscal a = new DocumentoFiscal("12345678901");
        DocumentoFiscal b = new DocumentoFiscal("12345678901");
        DocumentoFiscal c = new DocumentoFiscal("12345678000199");

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(c);
    }

    @Test
    @DisplayName("toString retorna o valor bruto")
    void toStringRetornaValor() {
        DocumentoFiscal doc = new DocumentoFiscal("12345678901");
        assertThat(doc.toString()).isEqualTo("12345678901");
    }
}
