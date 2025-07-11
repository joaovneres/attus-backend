package br.com.attus.processos.persistence.acao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import br.com.attus.processos.nucleo.application.port.out.acao.AcaoFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;

import java.time.LocalDate;
import java.util.Optional;

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
class AcaoJpaAdapterTest {

    @Mock
    AcaoRepository repo;

    private AcaoJpaAdapter adapter;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        adapter = new AcaoJpaAdapter(repo);
    }

    @Nested
    class FindById {

        @Test
        @DisplayName("findById delega ao repository e devolve Optional")
        void findById() {
            given(repo.findById(10L)).willReturn(Optional.of(new Acao()));

            Optional<Acao> result = adapter.findById(10L);

            assertThat(result).isPresent();
            then(repo).should().findById(10L);
        }
    }

    @Nested
    class Search {

        @Test
        @DisplayName("search passa Specification e Pageable corretos")
        void search() {
            AcaoFilter filter = new AcaoFilter(1L, TipoAcao.AUDIENCIA, LocalDate.now(), LocalDate.now());
            Pageable page = PageRequest.of(0, 10);

            given(repo.findAll((Example<Acao>) any(), eq(page)))
                    .willReturn(new PageImpl<>(java.util.List.of(new Acao())));

            var result = adapter.search(filter, page);

            assertThat(result.getContent()).hasSize(1);
            then(repo).should().findAll((Example<Acao>) any(), eq(page));
        }
    }

    @Nested
    class SaveFlush {

        @Test
        @DisplayName("save delega a saveAndFlush e retorna entidade persistida")
        void save() {
            Acao entity = new Acao();
            given(repo.saveAndFlush(entity)).willReturn(entity);

            Acao saved = adapter.save(entity);

            assertThat(saved).isSameAs(entity);
            then(repo).should().saveAndFlush(entity);
        }

        @Test
        @DisplayName("flush chama repo.flush()")
        void flush() {
            adapter.flush();
            then(repo).should().flush();
        }
    }
}
