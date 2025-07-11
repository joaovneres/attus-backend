/* src/test/java/br/com/attus/processos/nucleo/dominio/entidade/ParteTest.java */
package br.com.attus.processos.nucleo.dominio.entidade;

import static org.assertj.core.api.Assertions.*;

import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import br.com.attus.processos.nucleo.dominio.vo.Contato;
import br.com.attus.processos.nucleo.dominio.vo.DocumentoFiscal;
import java.time.LocalDate;
import org.junit.jupiter.api.*;

class ParteTest {

    private Processo processoBase() {
        return new Processo("PX-10", LocalDate.of(2025,1,1), "descr");
    }

    /* -------- Construtor -------- */

    @Test
    @DisplayName("Construtor deve exigir campos obrigatórios")
    void construtorValidacoes() {
        Processo proc = processoBase();

        assertThatThrownBy(() ->
                new Parte(null, "Ana",
                        new DocumentoFiscal("12345678901"),
                        TipoParte.AUTOR, null))
                .isInstanceOf(NullPointerException.class);

        assertThatCode(() ->
                new Parte(proc, "Ana",
                        new DocumentoFiscal("12345678901"),
                        TipoParte.AUTOR, null))
                .doesNotThrowAnyException();
    }

    /* ------ Atualizar contato ----- */

    @Test
    void atualizarContatoDefineNovoObjeto() {
        Parte p = new Parte(processoBase(), "Bob",
                new DocumentoFiscal("98765432100"),
                TipoParte.REU, null);

        Contato contato = new Contato("bob@mail.com", "1199");
        p.atualizarContato(contato);

        assertThat(p.getContato()).isSameAs(contato);
    }

    @Test
    void atualizarContatoPodeRemoverDados() {
        Parte p = new Parte(processoBase(), "Bob",
                new DocumentoFiscal("98765432100"),
                TipoParte.REU, new Contato("x@y.com", "123"));

        p.atualizarContato(null);
        assertThat(p.getContato()).isNull();
    }
}
