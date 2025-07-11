package br.com.attus.processos.consumer.persistence;

import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Long> {
    Optional<Processo> findByNumero(String numero);
}
