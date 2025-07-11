package br.com.attus.processos.persistence.processo;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.stream.Stream;

class ProcessoSpecificationTest {

    private static boolean producesPredicate(Specification<Processo> spec) {
        Root<Processo> root = Mockito.mock(Root.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        Predicate predicate = Mockito.mock(Predicate.class);
        Path<Object> path = Mockito.mock(Path.class);
        Join<Object, Object> join = Mockito.mock(Join.class);

        Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
        Mockito.when(cb.equal(Mockito.any(), Mockito.any())).thenReturn(predicate);
        Mockito.when(cb.greaterThanOrEqualTo(Mockito.any(), Mockito.any(Comparable.class))).thenReturn(predicate);
        Mockito.when(cb.lessThanOrEqualTo(Mockito.any(), Mockito.any(Comparable.class))).thenReturn(predicate);
        Mockito.when(cb.like(Mockito.any(), Mockito.anyString())).thenReturn(predicate);
        Mockito.when(cb.and(Mockito.any(Predicate[].class))).thenReturn(predicate);
        Mockito.when(cb.and(Mockito.any(Predicate.class), Mockito.any(Predicate.class))).thenReturn(predicate);

        Mockito.when(root.join(Mockito.anyString(), Mockito.any())).thenReturn(join);
        Mockito.when(root.join(Mockito.anyString())).thenReturn(join);
        Mockito.when(join.get(Mockito.anyString())).thenReturn(path);

        return spec.toPredicate(root, query, cb) != null;
    }


    private static Stream<Arguments> criterios() {
        return Stream.of(
                Arguments.of("status", new ProcessoFilter(StatusProcesso.ATIVO, null, null, null)),
                Arguments.of("intervalo datas", new ProcessoFilter(null,
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), null)),
                Arguments.of("apenas data inicial", new ProcessoFilter(null,
                        LocalDate.of(2025, 1, 1), null, null)),
                Arguments.of("apenas data final", new ProcessoFilter(null,
                        null, LocalDate.of(2025, 6, 30), null)),
                Arguments.of("cpfCnpj", new ProcessoFilter(null, null, null, "12345678901"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("criterios")
    void deveGerarPredicate(String nome, ProcessoFilter f) {
        assertThat(producesPredicate(ProcessoSpecification.of(f))).isTrue();
    }

    @Test
    @DisplayName("Filtro vazio gera Specification vazia (predicate nulo)")
    void filtroVazio() {
        var spec = ProcessoSpecification.of(new ProcessoFilter(null, null, null, "   "));
        assertThat(producesPredicate(spec)).isFalse();
    }
}
