/* src/test/java/br/com/attus/processos/persistence/processo/ProcessoRepositoryIT.java */
package br.com.attus.processos.persistence.processo;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import java.time.LocalDate;
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
@Import(ProcessoSpecification.class)
class ProcessoRepositoryIT {

    @Container
    private static final PostgreSQLContainer<?> pg =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("usr")
                    .withPassword("pwd");

    @DynamicPropertySource
    static void pgProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url",      pg::getJdbcUrl);
        r.add("spring.datasource.username", pg::getUsername);
        r.add("spring.datasource.password", pg::getPassword);
    }

    @Autowired ProcessoRepository repo;

    @Test
    @DisplayName("save, findById e search com Specification")
    void fluxoCompleto() {
        /* ---------- Arrange ---------- */
        Processo p1 = new Processo("P-001", LocalDate.of(2025, 1, 2), "aberto");
        Processo p2 = new Processo("P-002", LocalDate.of(2025, 3, 15), "suspenso");
        p2.suspender();               // muda status p/ SUSPENSO
        repo.save(p1);
        repo.save(p2);

        /* ---------- Act & Assert ---------- */
        assertThat(repo.findById(p1.getId())).isPresent();

        ProcessoFilter f = new ProcessoFilter(
                StatusProcesso.SUSPENSO,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                null);

        var page = repo.findAll(
                ProcessoSpecification.of(f), PageRequest.of(0, 10));

        assertThat(page.getContent())
                .extracting(Processo::getNumero)
                .containsExactly("P-002");
    }
}
