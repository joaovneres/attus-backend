package br.com.attus.processos.application.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class AcaoDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static Stream<Scenario> invalidScenarios() {
        return Stream.of(
                new Scenario(null, TipoAcao.PETICAO, LocalDate.now(), "ok", "processoId é obrigatório"),
                new Scenario(1L, null, LocalDate.now(), "ok", "tipo é obrigatório"),
                new Scenario(1L, TipoAcao.SENTENCA, null, "","dataRegistro é obrigatória")
        );
    }

    @ParameterizedTest(name = "[{index}] {4}")
    @MethodSource("invalidScenarios")
    void deveFalharValidacaoCamposObrigatorios(Scenario s) {
        var dto = new AcaoDto.CreateRequest(s.processoId, s.tipo, s.dataRegistro, s.descricao);
        Set<ConstraintViolation<AcaoDto.CreateRequest>> violations = validator.validate(dto);
        assertThat(violations)
                .anyMatch(v -> v.getMessage().equals(s.expectedMessage));
    }

    private record Scenario(Long processoId,
                            TipoAcao tipo,
                            LocalDate dataRegistro,
                            String descricao,
                            String expectedMessage) { }
}
