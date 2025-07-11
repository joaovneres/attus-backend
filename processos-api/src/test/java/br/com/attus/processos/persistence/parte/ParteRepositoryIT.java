/* src/test/java/br/com/attus/processos/persistence/parte/ParteRepositoryIT.java */
package br.com.attus.processos.persistence.parte;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.attus.processos.nucleo.application.port.out.parte.ParteFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
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
@Import(ParteSpecification.class)         // injeta a spec utilitária
class ParteRepositoryIT {

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

    @Autowired  ParteRepository  repo;
    @Autowired  jakarta.persistence.EntityManager em;

    @Test
    @DisplayName("save • findById • search por filtro composto")
    void fluxoCompleto() {
        /* ---------- Arrange ---------- */
        Processo proc = new Processo("PX-01", LocalDate.of(2025,1,1), null);
        em.persist(proc);

        Parte p1 = new Parte(proc, "Ana",  new br.com.attus.processos.nucleo.dominio.vo.DocumentoFiscal("12345678901"),
                TipoParte.AUTOR,    null);
        Parte p2 = new Parte(proc, "Bob",  new br.com.attus.processos.nucleo.dominio.vo.DocumentoFiscal("98765432100"),
                TipoParte.REU,      null);
        repo.saveAll(List.of(p1, p2));

        /* ---------- Act ---------- */
        var buscada = repo.findById(p1.getId());

        ParteFilter filter = new ParteFilter(proc.getId(), TipoParte.AUTOR,
                "12345678901", "Ana");
        var page = repo.findAll(ParteSpecification.of(filter), PageRequest.of(0,10));

        /* ---------- Assert ---------- */
        assertThat(buscada).isPresent();
        assertThat(page.getContent())
                .extracting(Parte::getNome)
                .containsExactly("Ana");
    }

    @Test
    @DisplayName("filtro vazio retorna todas as partes do processo")
    void filtroVazio() {
        Processo proc = new Processo("PX-02", LocalDate.now(), null);
        em.persist(proc);

        repo.save(new Parte(proc, "Carlos",
                new br.com.attus.processos.nucleo.dominio.vo.DocumentoFiscal("11111111111"),
                TipoParte.ADVOGADO, null));

        var resultados = repo.findAll(
                ParteSpecification.of(new ParteFilter(proc.getId(), null, null, null)),
                PageRequest.of(0, 10));

        assertThat(resultados.getTotalElements()).isEqualTo(1);
    }
}
