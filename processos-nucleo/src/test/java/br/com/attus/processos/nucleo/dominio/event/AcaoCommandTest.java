/* src/test/java/br/com/attus/processos/nucleo/dominio/event/AcaoCommandTest.java */
package br.com.attus.processos.nucleo.dominio.event;

import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AcaoCommandTest {

    @Test
    @DisplayName("Create deve construir instância válida")
    void createSuccess() {
        LocalDate hoje = LocalDate.now();
        AcaoCommand.Create cmd = new AcaoCommand.Create(1L, TipoAcao.PETICAO, hoje, "descrição");
        assertThat(cmd.processoId()).isEqualTo(1L);
        assertThat(cmd.tipo()).isEqualTo(TipoAcao.PETICAO);
        assertThat(cmd.dataRegistro()).isEqualTo(hoje);
        assertThat(cmd.descricao()).isEqualTo("descrição");
    }

    @Test
    @DisplayName("Create deve falhar se processoId for nulo")
    void createNullProcessoId() {
        assertThatThrownBy(() ->
                new AcaoCommand.Create(null, TipoAcao.PETICAO, LocalDate.now(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("processoId não pode ser nulo");
    }

    @Test
    @DisplayName("Create deve falhar se processoId ≤ 0")
    void createInvalidProcessoId() {
        assertThatThrownBy(() ->
                new AcaoCommand.Create(0L, TipoAcao.AUDIENCIA, LocalDate.now(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("processoId deve ser > 0");
    }

    @Test
    @DisplayName("Create deve falhar se tipo for nulo")
    void createNullTipo() {
        assertThatThrownBy(() ->
                new AcaoCommand.Create(1L, null, LocalDate.now(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("tipo não pode ser nulo");
    }

    @Test
    @DisplayName("Create deve falhar se dataRegistro for nula")
    void createNullDataRegistro() {
        assertThatThrownBy(() ->
                new AcaoCommand.Create(1L, TipoAcao.SENTENCA, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("dataRegistro não pode ser nulo");
    }
}
