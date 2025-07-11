/* src/test/java/br/com/attus/processos/nucleo/dominio/entidade/AcaoTest.java */
package br.com.attus.processos.nucleo.dominio.entidade;

import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AcaoTest {

    private final Processo proc = new Processo(
            "001", LocalDate.of(2025, 1, 1), null);

    /* ---------- Construtor ---------- */

    @Nested
    class Construtor {

        @Test
        @DisplayName("Sucesso quando todos argumentos válidos")
        void construtorOk() {
            Acao a = new Acao(proc, TipoAcao.AUDIENCIA,
                    LocalDate.now(), "descricao");
            assertThat(a.getProcesso()).isSameAs(proc);
            assertThat(a.getTipo()).isEqualTo(TipoAcao.AUDIENCIA);
        }

        @Test
        void deveFalharQuandoProcessoNulo() {
            assertThatThrownBy(() ->
                    new Acao(null, TipoAcao.PETICAO, LocalDate.now(), null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void deveFalharQuandoDataNoFuturo() {
            LocalDate amanha = LocalDate.now().plusDays(1);
            assertThatThrownBy(() ->
                    new Acao(proc, TipoAcao.PETICAO, amanha, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("futuro");
        }
    }

    /* ---------- Mutação ---------- */

    @Test
    void alterarDescricaoAtualizaCampo() {
        Acao a = new Acao(proc, TipoAcao.SENTENCA,
                LocalDate.now(), "old");
        a.alterarDescricao("new");

        assertThat(a.getDescricao()).isEqualTo("new");
    }
}
