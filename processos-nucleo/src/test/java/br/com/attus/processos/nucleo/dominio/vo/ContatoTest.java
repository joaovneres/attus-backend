/* src/test/java/br/com/attus/processos/nucleo/dominio/vo/ContatoTest.java */
package br.com.attus.processos.nucleo.dominio.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ContatoTest {

    @Test
    @DisplayName("Construtor deve atribuir email e telefone corretamente")
    void constructorAssignsFields() {
        Contato c = new Contato("user@example.com", "1234-5678");
        assertThat(c.getEmail()).isEqualTo("user@example.com");
        assertThat(c.getTelefone()).isEqualTo("1234-5678");
    }

    @Test
    @DisplayName("Equals/hashCode devem considerar email e telefone")
    void equalsAndHashCode() {
        Contato a = new Contato("x@x.com", "1111");
        Contato b = new Contato("x@x.com", "1111");
        Contato c = new Contato("y@y.com", "2222");

        // reflexivo
        assertThat(a).isEqualTo(a);
        // simétrico
        assertThat(a).isEqualTo(b);
        assertThat(b).isEqualTo(a);
        // transitive
        Contato b2 = new Contato("x@x.com", "1111");
        assertThat(a).isEqualTo(b2);
        // desigualdade
        assertThat(a).isNotEqualTo(c);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.hashCode()).isNotEqualTo(c.hashCode());
    }

    @Test
    @DisplayName("ToString inclui campos email e telefone")
    void toStringIncludesFields() {
        Contato c = new Contato("foo@bar", "9999");
        String s = c.toString();
        assertThat(s)
                .contains("email=foo@bar")
                .contains("telefone=9999");
    }

    @Test
    @DisplayName("Construtor aceita valores nulos ou vazios sem lançar exceção")
    void constructorAllowsNullsAndEmpty() {
        assertThatCode(() -> new Contato(null, null)).doesNotThrowAnyException();
        assertThatCode(() -> new Contato("", "")).doesNotThrowAnyException();
    }
}
