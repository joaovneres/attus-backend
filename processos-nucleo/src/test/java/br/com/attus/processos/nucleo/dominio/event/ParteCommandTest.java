/* src/test/java/br/com/attus/processos/nucleo/dominio/event/ParteCommandTest.java */
package br.com.attus.processos.nucleo.dominio.event;

import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ParteCommandTest {

    @Nested
    @DisplayName("Create")
    class CreateTests {

        @Test
        @DisplayName("deve construir instância válida")
        void createSuccess() {
            var cmd = new ParteCommand.Create(
                    1L, "Ana", "12345678901",
                    TipoParte.AUTOR, "ana@mail.com", "1199");
            assertThat(cmd.processoId()).isEqualTo(1L);
            assertThat(cmd.nome()).isEqualTo("Ana");
            assertThat(cmd.cpfCnpj()).isEqualTo("12345678901");
            assertThat(cmd.tipo()).isEqualTo(TipoParte.AUTOR);
        }

        @Test
        @DisplayName("deve falhar se processoId for nulo ou ≤0")
        void createInvalidProcessoId() {
            assertThatThrownBy(() ->
                    new ParteCommand.Create(null, "Ana", "123", TipoParte.REU, null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("processoId não pode ser nulo");

            assertThatThrownBy(() ->
                    new ParteCommand.Create(0L, "Ana", "123", TipoParte.REU, null, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("processoId deve ser > 0");
        }

        @Test
        @DisplayName("deve falhar se nome ou cpfCnpj forem nulos/vazios")
        void createInvalidStrings() {
            assertThatThrownBy(() ->
                    new ParteCommand.Create(1L, null, "123", TipoParte.REU, null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("nome não pode ser nulo");

            assertThatThrownBy(() ->
                    new ParteCommand.Create(1L, " ", "123", TipoParte.REU, null, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("nome não pode ser vazio");

            assertThatThrownBy(() ->
                    new ParteCommand.Create(1L, "Ana", null, TipoParte.REU, null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("cpfCnpj não pode ser nulo");

            assertThatThrownBy(() ->
                    new ParteCommand.Create(1L, "Ana", " ", TipoParte.REU, null, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cpfCnpj não pode ser vazio");
        }

        @Test
        @DisplayName("deve falhar se tipo for nulo")
        void createNullTipo() {
            assertThatThrownBy(() ->
                    new ParteCommand.Create(1L, "Ana", "123", null, null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("tipo não pode ser nulo");
        }
    }

    @Nested
    @DisplayName("UpdateContato")
    class UpdateContatoTests {

        @Test
        @DisplayName("deve construir instância válida")
        void updateSuccess() {
            var cmd = new ParteCommand.UpdateContato(5L, "bob@mail.com", "1199");
            assertThat(cmd.parteId()).isEqualTo(5L);
            assertThat(cmd.email()).isEqualTo("bob@mail.com");
        }

        @Test
        @DisplayName("deve falhar se parteId for nulo ou ≤0")
        void updateInvalidParteId() {
            assertThatThrownBy(() ->
                    new ParteCommand.UpdateContato(null, null, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("parteId não pode ser nulo");

            assertThatThrownBy(() ->
                    new ParteCommand.UpdateContato(0L, null, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("parteId deve ser > 0");
        }
    }
}
