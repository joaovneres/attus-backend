/* src/test/java/br/com/attus/processos/nucleo/application/port/out/parte/ParteFilterTest.java */
package br.com.attus.processos.nucleo.application.port.out.parte;

import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParteFilterTest {

    @Test
    void isEmpty_retornaTrue_quandoTodosCamposNulosOuVazios() {
        ParteFilter f1 = new ParteFilter(null, null, null, null);
        ParteFilter f2 = new ParteFilter(null, null, "   ", " ");  // só espaços

        assertThat(f1.isEmpty()).isTrue();
        assertThat(f2.isEmpty()).isTrue();
    }

    @Test
    void isEmpty_retornaFalse_quandoAlgumCampoPreenchido() {
        ParteFilter f = new ParteFilter(1L, TipoParte.AUTOR, null, null);

        assertThat(f.isEmpty()).isFalse();
    }
}
