package br.com.attus.processos.persistence.parte;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import br.com.attus.processos.nucleo.application.port.out.parte.ParteFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class ParteJpaAdapterTest {

    @Mock ParteRepository repo;
    @Mock ParteSpecification spec;

    ParteJpaAdapter adapter;

    @BeforeEach
    void init() {
        adapter = new ParteJpaAdapter(repo);
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        void deveDelegarParaRepositorio() {
            Parte parte = new Parte();
            given(repo.findById(3L)).willReturn(Optional.of(parte));

            Optional<Parte> result = adapter.findById(3L);

            assertThat(result).contains(parte);
            then(repo).should().findById(3L);
        }
    }

    @Nested
    @DisplayName("save & flush")
    class SaveFlush {

        @Test
        void deveSalvarEFlushar() {
            Parte parte = new Parte();
            given(repo.saveAndFlush(parte)).willReturn(parte);

            Parte saved = adapter.save(parte);

            assertThat(saved).isSameAs(parte);
            then(repo).should().saveAndFlush(parte);
        }

        @Test
        void deveChamarFlush() {
            adapter.flush();
            then(repo).should().flush();
        }
    }

    @Nested
    @DisplayName("search")
    class Search {

        @Test
        void deveAplicarSpecificationEPageable() {
            ParteFilter f = new ParteFilter(10L, TipoParte.REU, "123", "Jo");
            Pageable page = PageRequest.of(0, 5);

            given(spec.of(f)).willReturn((Specification<Parte>) (root, query, cb) -> null);
            given(repo.findAll(any(Specification.class), eq(page)))
                    .willReturn(new PageImpl<>(List.of(new Parte())));

            var result = adapter.search(f, page);

            assertThat(result.getContent()).hasSize(1);
            then(spec).should().of(f);
            then(repo).should().findAll(any(Specification.class), eq(page));
        }
    }
}