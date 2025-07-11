package br.com.attus.processos.nucleo.config;

import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProcessoJpaAdapterIT {

    @Autowired
    private ProcessoRepositoryPort repo;

    @Test
    void deveSalvarERecuperarProcesso() {
        Processo p = new Processo("123-ABC", LocalDate.now(), "Teste");
        Processo salvo = repo.save(p);

        Optional<Processo> buscado = repo.findById(salvo.getId());
        assertThat(buscado).isPresent();
        assertThat(buscado.get().getNumero()).isEqualTo("123-ABC");
    }
}
