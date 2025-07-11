package br.com.attus.processos.application.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ProcessoDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static Stream<Scenario> invalidScenarios() {
        return Stream.of(
                new Scenario("", LocalDate.now(), "x", "numero é obrigatório"),
                new Scenario("x".repeat(31), LocalDate.now(), null, "numero deve ter até 30 caracteres"),
                new Scenario("PROC01", null, null, "dataAbertura é obrigatória"),
                new Scenario("PROC01", LocalDate.now().plusDays(1), null, "dataAbertura não pode estar no futuro")
        );
    }

    @ParameterizedTest(name = "[{index}] {3}")
    @MethodSource("invalidScenarios")
    void deveFalharValidacaoCamposObrigatorios(Scenario s) {
        var dto = new ProcessoDto.CreateRequest(s.numero, s.dataAbertura, s.descricao);
        Set<ConstraintViolation<ProcessoDto.CreateRequest>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getMessage().equals(s.expectedMessage));
    }

    private static Stream<Scenario> validScenario() {
        return Stream.of(
                new Scenario("PROC01", LocalDate.now(), "descrição curta", null)
        );
    }

    @ParameterizedTest(name = "Cenário válido deve passar na validação")
    @MethodSource("validScenario")
    void devePassarValidacao(Scenario s) {
        var dto = new ProcessoDto.CreateRequest(s.numero, s.dataAbertura, s.descricao);
        assertThat(validator.validate(dto)).isEmpty();
    }

    private record Scenario(String numero,
                            LocalDate dataAbertura,
                            String descricao,
                            String expectedMessage) { }
}