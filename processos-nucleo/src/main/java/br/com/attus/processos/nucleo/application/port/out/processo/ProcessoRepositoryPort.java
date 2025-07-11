package br.com.attus.processos.nucleo.application.port.out.processo;

import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProcessoRepositoryPort {

    Optional<Processo> findById(Long id);

    Processo save(Processo processo);

    Page<Processo> search(ProcessoFilter filter, Pageable pageable);

    void flush();
}
