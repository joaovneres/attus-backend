package br.com.attus.processos.persistence.acao;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.attus.processos.nucleo.application.port.out.acao.AcaoFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@Import(AcaoSpecification.class)
class AcaoRepositoryIT {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("user")
                    .withPassword("pass");

    @DynamicPropertySource
    static void pgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    AcaoRepository repo;

    @Autowired
    jakarta.persistence.EntityManager em;

    @Test
    @DisplayName("save • findById • search com Specification")
    void repositoryFlow() {
        Processo proc = new Processo("001", LocalDate.of(2025, 1, 1), "DESC");
        em.persist(proc);

        Acao acao = new Acao(proc, TipoAcao.AUDIENCIA, LocalDate.of(2025, 7, 11), "primeira");
        repo.saveAndFlush(acao);

        var fetched = repo.findById(acao.getId());

        var filtro = new AcaoFilter(proc.getId(), TipoAcao.AUDIENCIA,
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));

        var page = repo.findAll(AcaoSpecification.of(filtro), PageRequest.of(0, 10));

        assertThat(fetched).isPresent();
        assertThat(page.getContent()).extracting(Acao::getDescricao).containsExactly("primeira");
    }

    @Test
    @DisplayName("search vazio retorna todas as ações do processo")
    void searchSemFiltro() {
        Processo proc = new Processo("002", LocalDate.of(2024, 12, 12), null);
        em.persist(proc);

        List.of(TipoAcao.PETICAO, TipoAcao.SENTENCA).forEach(t ->
                repo.save(new Acao(proc, t, LocalDate.now(), t.name())));

        var page = repo.findAll(
                AcaoSpecification.of(new AcaoFilter(proc.getId(), null, null, null)),
                PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(2);
    }
}