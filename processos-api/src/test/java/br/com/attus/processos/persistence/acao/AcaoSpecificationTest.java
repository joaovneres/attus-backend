package br.com.attus.processos.persistence.acao;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.attus.processos.nucleo.application.port.out.acao.AcaoFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.time.LocalDate;

class AcaoSpecificationTest {

    private boolean producesPredicate(Specification<Acao> spec) {
        Root<Acao> root = Mockito.mock(Root.class);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        Predicate predicate = Mockito.mock(Predicate.class);
        Path<Object> path = Mockito.mock(Path.class);
        Join<Object, Object> join = Mockito.mock(Join.class);

        Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
        Mockito.when(root.join(Mockito.anyString(), Mockito.any())).thenReturn(join);
        Mockito.when(join.get(Mockito.anyString())).thenReturn(path);

        Mockito.when(cb.equal(Mockito.any(), Mockito.any())).thenReturn(predicate);
        Mockito.when(cb.greaterThanOrEqualTo(Mockito.any(), Mockito.any(Comparable.class))).thenReturn(predicate);
        Mockito.when(cb.lessThanOrEqualTo(Mockito.any(), Mockito.any(Comparable.class))).thenReturn(predicate);
        Mockito.when(cb.and(Mockito.any(Predicate[].class))).thenReturn(predicate);
        Mockito.when(cb.and(Mockito.any(Predicate.class), Mockito.any(Predicate.class))).thenReturn(predicate);

        return spec != null && spec.toPredicate(root, query, cb) != null;
    }

    @Nested
    @DisplayName("Filtro de processoId")
    class ProcessoId {

        @Test
        void deveCriarPredicateQuandoProcessoInformado() {
            var filter = new AcaoFilter(99L, null, null, null);
            assertThat(producesPredicate(AcaoSpecification.of(filter))).isTrue();
        }

        @Test
        void naoCriaPredicateQuandoProcessoNull() {
            var filter = new AcaoFilter(null, null, null, null);
            assertThat(producesPredicate(AcaoSpecification.of(filter))).isFalse();
        }
    }

    @Nested
    @DisplayName("Filtro de tipo de ação")
    class TipoAcaoFilter {

        @Test
        void geraPredicateQuandoTipoInformado() {
            var filter = new AcaoFilter(null, TipoAcao.PETICAO, null, null);
            assertThat(producesPredicate(AcaoSpecification.of(filter))).isTrue();
        }
    }

    @Nested
    @DisplayName("Filtro por datas")
    class DatasFilter {

        @Test
        void intervaloCompleto() {
            var filter = new AcaoFilter(null, null, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));
            assertThat(producesPredicate(AcaoSpecification.of(filter))).isTrue();
        }

        @Test
        void somenteDataInicial() {
            var filter = new AcaoFilter(null, null, LocalDate.of(2025, 1, 1), null);
            assertThat(producesPredicate(AcaoSpecification.of(filter))).isTrue();
        }

        @Test
        void somenteDataFinal() {
            var filter = new AcaoFilter(null, null, null, LocalDate.of(2025, 6, 30));
            assertThat(producesPredicate(AcaoSpecification.of(filter))).isTrue();
        }
    }

    @Test
    @DisplayName("Todos os filtros nulos → Specification vazia")
    void specVazia() {
        var filter = new AcaoFilter(null, null, null, null);
        assertThat(producesPredicate(AcaoSpecification.of(filter))).isFalse();
    }
}
