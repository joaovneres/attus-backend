/* src/test/java/br/com/attus/processos/nucleo/dominio/entidade/EntidadeBaseTest.java */
package br.com.attus.processos.nucleo.dominio.entidade;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Stub simples para testar comportamento comum da superclasse. */
class StubEntity extends EntidadeBase { }

class EntidadeBaseTest {

    @Test
    @DisplayName("equals/hashCode usam ID quando não-nulo")
    void equalityById() {
        StubEntity a = new StubEntity();
        StubEntity b = new StubEntity();

        // ainda sem ID → não iguais
        assertThat(a).isNotEqualTo(b);

        // simula JPA atribuindo IDs
        java.lang.reflect.Field id = getIdField();
        try {
            id.setAccessible(true);
            id.set(a, 1L);
            id.set(b, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("desativar() altera flag ativo para false")
    void softDelete() {
        StubEntity e = new StubEntity();
        assertThat(e.isAtivo()).isTrue();

        e.desativar();
        assertThat(e.isAtivo()).isFalse();
    }

    /* utilitário reflection para acessar campo privado */
    private java.lang.reflect.Field getIdField() {
        try {
            return EntidadeBase.class.getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
