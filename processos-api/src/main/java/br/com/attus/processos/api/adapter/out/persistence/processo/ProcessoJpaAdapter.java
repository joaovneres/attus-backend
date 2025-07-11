package br.com.attus.processos.api.adapter.out.persistence.processo;

import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class ProcessoJpaAdapter implements ProcessoRepositoryPort {

    private final SpringDataProcessoRepository repo;
    private final ProcessoSpecification        spec;

    @Override public Optional<Processo> findById(Long id) {
        return repo.findById(id);
    }

    @Override public Processo save(Processo processo) {
        return repo.saveAndFlush(processo);
    }

    @Override public Page<Processo> search(ProcessoFilter filter, Pageable pageable) {
        return repo.findAll(spec.toSpec(filter), pageable);
    }

    @Override public void flush() { repo.flush(); }
}
