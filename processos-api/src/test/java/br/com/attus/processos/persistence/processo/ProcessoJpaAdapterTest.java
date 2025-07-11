package br.com.attus.processos.persistence.processo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class ProcessoJpaAdapterTest {

    @Mock
    ProcessoRepository repo;
    @Mock
    ProcessoSpecification spec;

    ProcessoJpaAdapter adapter;

    @BeforeEach
    void init() {
        adapter = new ProcessoJpaAdapter(repo);
    }

    @Nested
    class FindById {

        @Test
        @DisplayName("Delegar findById ao repository")
        void find() {
            Processo p = new Processo();
            given(repo.findById(1L)).willReturn(Optional.of(p));

            Optional<Processo> result = adapter.findById(1L);

            assertThat(result).contains(p);
            then(repo).should().findById(1L);
        }
    }

    @Nested
    class SaveFlush {

        @Test
        void save() {
            Processo p = new Processo();
            given(repo.saveAndFlush(p)).willReturn(p);

            Processo saved = adapter.save(p);

            assertThat(saved).isSameAs(p);
            then(repo).should().saveAndFlush(p);
        }

        @Test
        void flush() {
            adapter.flush();
            then(repo).should().flush();
        }
    }

    @Nested
    class Search {

        @Test
        void searchComFiltro() {
            ProcessoFilter f = new ProcessoFilter(StatusProcesso.ATIVO,
                    LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 12, 31),
                    "123");
            Pageable pg = PageRequest.of(0, 20);

            given(spec.of(f)).willReturn((root, query, criteriaBuilder) -> null);
            given(repo.findAll((Example<Processo>) any(), eq(pg)))
                    .willReturn(new PageImpl<>(List.of(new Processo())));

            var page = adapter.search(f, pg);

            assertThat(page.getTotalElements()).isEqualTo(1);
            then(spec).should().of(f);
            then(repo).should().findAll((Example<Processo>) any(), eq(pg));
        }
    }
}
