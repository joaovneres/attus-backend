package br.com.attus.processos.application.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ParteDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static Stream<Scenario> invalidScenarios() {
        return Stream.of(
                new Scenario(null, "Ana", "12345678901", TipoParte.AUTOR,
                        "ana@mail.com", "1199", "processoId é obrigatório"),

                new Scenario(1L, "", "12345678901", TipoParte.AUTOR,
                        null, null, "nome é obrigatório"),

                new Scenario(1L, "Ana", "", TipoParte.AUTOR,
                        null, null, "cpfCnpj é obrigatório"),

                new Scenario(1L, "Ana", "abcdef", TipoParte.AUTOR,
                        null, null, "cpfCnpj deve conter 11 ou 14 dígitos"),

                new Scenario(1L, "Ana", "12345678901", null,
                        null, null, "tipo é obrigatório"),

                new Scenario(1L, "Ana", "12345678901", TipoParte.REU,
                        "mail-invalido", null, "email inválido")
        );
    }

    @ParameterizedTest(name = "[{index}] {6}")
    @MethodSource("invalidScenarios")
    void deveFalharValidacao(Scenario s) {
        var dto = new ParteDto.CreateRequest(
                s.processoId, s.nome, s.cpfCnpj, s.tipo, s.email, s.telefone);

        Set<ConstraintViolation<ParteDto.CreateRequest>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getMessage().equals(s.expectedMessage));
    }

    private record Scenario(Long processoId,
                            String nome,
                            String cpfCnpj,
                            TipoParte tipo,
                            String email,
                            String telefone,
                            String expectedMessage) { }
}