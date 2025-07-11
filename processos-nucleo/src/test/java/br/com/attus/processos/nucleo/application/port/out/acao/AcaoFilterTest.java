package br.com.attus.processos.nucleo.application.port.out.acao;

import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AcaoFilterTest {

    @Test
    @DisplayName("isEmpty deve ser true quando todos os campos são nulos")
    void shouldBeEmpty() {
        AcaoFilter f = new AcaoFilter(null, null, null, null);
        assertThat(f.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("isEmpty deve ser false quando algum campo é preenchido")
    void shouldNotBeEmpty() {
        AcaoFilter f = new AcaoFilter(1L, TipoAcao.PETICAO,
                LocalDate.of(2025,1,1), null);
        assertThat(f.isEmpty()).isFalse();
    }
}
