package br.com.attus.processos.consumidor.adapter.out.persistence;

import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProcessoRepository extends JpaRepository<Processo, Long> {

    Optional<Processo> findByNumero(String numero);
}
