/* src/test/java/br/com/attus/processos/nucleo/dominio/event/ProcessoCommandTest.java */
package br.com.attus.processos.nucleo.dominio.event;

import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProcessoCommandTest {

    @Nested
    @DisplayName("Create")
    class CreateTests {

        @Test
        @DisplayName("sucesso quando campos válidos")
        void createSuccess() {
            var cmd = new ProcessoCommand.Create(
                    "123-XYZ",
                    LocalDate.now().minusDays(1),
                    "descrição");
            assertThat(cmd.numero()).isEqualTo("123-XYZ");
            assertThat(cmd.dataAbertura()).isEqualTo(LocalDate.now().minusDays(1));
            assertThat(cmd.descricao()).isEqualTo("descrição");
        }

        @Test
        @DisplayName("falha se numero nulo ou vazio")
        void createInvalidNumero() {
            assertThatThrownBy(() ->
                    new ProcessoCommand.Create(null,
                            LocalDate.now(), null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("numero não pode ser nulo");

            assertThatThrownBy(() ->
                    new ProcessoCommand.Create("   ",
                            LocalDate.now(), null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("numero não pode ser vazio");
        }

        @Test
        @DisplayName("falha se dataAbertura nula ou futura")
        void createInvalidData() {
            assertThatThrownBy(() ->
                    new ProcessoCommand.Create("001", null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("dataAbertura não pode ser nula");

            assertThatThrownBy(() ->
                    new ProcessoCommand.Create("001",
                            LocalDate.now().plusDays(1), null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("dataAbertura não pode estar no futuro");
        }
    }

    @Nested
    @DisplayName("UpdateDesc")
    class UpdateDescTests {

        @Test
        @DisplayName("sucesso quando id válido")
        void updateSuccess() {
            var cmd = new ProcessoCommand.UpdateDesc(5L, "nova descrição");
            assertThat(cmd.id()).isEqualTo(5L);
            assertThat(cmd.descricao()).isEqualTo("nova descrição");
        }

        @Test
        @DisplayName("falha se id nulo ou ≤0")
        void updateInvalidId() {
            assertThatThrownBy(() ->
                    new ProcessoCommand.UpdateDesc(null, "x"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("id não pode ser nulo");

            assertThatThrownBy(() ->
                    new ProcessoCommand.UpdateDesc(0L, "x"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("id deve ser > 0");
        }
    }

    @Nested
    @DisplayName("ChangeStatus")
    class ChangeStatusTests {

        @Test
        @DisplayName("sucesso quando dados válidos")
        void statusSuccess() {
            var cmd = new ProcessoCommand.ChangeStatus(7L, StatusProcesso.ARQUIVADO);
            assertThat(cmd.id()).isEqualTo(7L);
            assertThat(cmd.novoStatus()).isEqualTo(StatusProcesso.ARQUIVADO);
        }

        @Test
        @DisplayName("falha se id nulo ou ≤0")
        void statusInvalidId() {
            assertThatThrownBy(() ->
                    new ProcessoCommand.ChangeStatus(null, StatusProcesso.ATIVO))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("id não pode ser nulo");

            assertThatThrownBy(() ->
                    new ProcessoCommand.ChangeStatus(0L, StatusProcesso.ATIVO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("id deve ser > 0");
        }

        @Test
        @DisplayName("falha se novoStatus for nulo")
        void statusNullStatus() {
            assertThatThrownBy(() ->
                    new ProcessoCommand.ChangeStatus(1L, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("novoStatus não pode ser nulo");
        }
    }
}
