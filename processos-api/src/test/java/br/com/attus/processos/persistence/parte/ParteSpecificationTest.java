package br.com.attus.processos.persistence.parte;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.attus.processos.nucleo.application.port.out.parte.ParteFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.util.function.Function;
import java.util.stream.Stream;

class ParteSpecificationTest {

    private static boolean hasPredicate(Specification<Parte> spec) {
        Root<Parte> root = Mockito.mock(Root.class);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        Predicate predicate = Mockito.mock(Predicate.class);
        Path<Object> path = Mockito.mock(Path.class);

        Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
        Mockito.when(cb.equal(Mockito.any(), Mockito.any())).thenReturn(predicate);
        Mockito.when(cb.like(Mockito.any(), Mockito.anyString())).thenReturn(predicate);
        Mockito.when(cb.and(Mockito.any(Predicate[].class))).thenReturn(predicate);

        return spec.toPredicate(root, query, cb) != null;
    }

    private static Stream<Arguments> scenarios() {
        return Stream.of(
                Arguments.of(Named.of("processoId presente", (Function<Void, ParteFilter>) v ->
                        new ParteFilter(1L, null, null, null))),
                Arguments.of(Named.of("tipo presente", (Function<Void, ParteFilter>) v ->
                        new ParteFilter(null, TipoParte.ADVOGADO, null, null))),
                Arguments.of(Named.of("cpfCnpj presente", (Function<Void, ParteFilter>) v ->
                        new ParteFilter(null, null, "12345678901", null))),
                Arguments.of(Named.of("nome presente", (Function<Void, ParteFilter>) v ->
                        new ParteFilter(null, null, null, "Maria")))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("scenarios")
    @DisplayName("deve gerar predicate quando algum campo está preenchido")
    void shouldGeneratePredicate(Function<Void, ParteFilter> builder) {
        var filter = builder.apply(null);
        var spec = ParteSpecification.of(filter);
        assertThat(hasPredicate(spec)).isTrue();
    }

    @Test
    @DisplayName("todos os campos nulos/vazios → predicate nulo")
    void emptyFilter() {
        var spec = ParteSpecification.of(new ParteFilter(null, null, null, "  "));
        assertThat(hasPredicate(spec)).isFalse();
    }
}
