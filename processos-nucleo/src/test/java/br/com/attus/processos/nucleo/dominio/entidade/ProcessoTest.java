/* src/test/java/br/com/attus/processos/nucleo/dominio/entidade/ProcessoTest.java */
package br.com.attus.processos.nucleo.dominio.entidade;

import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ProcessoTest {

    /* ----------- Construtor ----------- */

    @Test
    void deveCriarProcessoValido() {
        Processo p = new Processo("001",
                LocalDate.now().minusDays(1),
                "descr");
        assertThat(p.getStatus()).isEqualTo(StatusProcesso.ATIVO);
    }

    @Test
    void deveFalharDataAberturaFutura() {
        assertThatThrownBy(() ->
                new Processo("002",
                        LocalDate.now().plusDays(1),
                        null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /* ----------- Transições ----------- */

    @Test
    void suspenderEReabrir() {
        Processo p = new Processo("003",
                LocalDate.now(),
                null);

        p.suspender();
        assertThat(p.getStatus()).isEqualTo(StatusProcesso.SUSPENSO);

        p.reabrir();
        assertThat(p.getStatus()).isEqualTo(StatusProcesso.ATIVO);
    }

    @Test
    void arquivarSomenteUmaVez() {
        Processo p = new Processo("004",
                LocalDate.now(),
                null);

        p.arquivar();
        assertThat(p.getStatus()).isEqualTo(StatusProcesso.ARQUIVADO);

        assertThatThrownBy(p::arquivar)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void suspenderSomenteSeAtivo() {
        Processo p = new Processo("005",
                LocalDate.now(),
                null);
        p.arquivar();

        assertThatThrownBy(p::suspender)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reabrirSomenteSeSuspenso() {
        Processo p = new Processo("006",
                LocalDate.now(),
                null);

        assertThatThrownBy(p::reabrir)
                .isInstanceOf(IllegalStateException.class);
    }
}
