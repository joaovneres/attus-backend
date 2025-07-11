/* src/test/java/br/com/attus/processos/nucleo/application/port/out/processo/ProcessoFilterTest.java */
package br.com.attus.processos.nucleo.application.port.out.processo;

import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ProcessoFilterTest {

    @Test
    void isEmpty_deveRetornarTrue_quandoTodosCamposNulosOuVazios() {
        ProcessoFilter f1 = new ProcessoFilter(null, null, null, null);
        ProcessoFilter f2 = new ProcessoFilter(null, null, null, "  ");

        assertThat(f1.isEmpty()).isTrue();
        assertThat(f2.isEmpty()).isTrue();
    }

    @Test
    void isEmpty_deveRetornarFalse_quandoAlgumCampoPreenchido() {
        ProcessoFilter f = new ProcessoFilter(StatusProcesso.ATIVO,
                LocalDate.of(2025, 1, 1), null, null);

        assertThat(f.isEmpty()).isFalse();
    }
}
