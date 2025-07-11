package br.com.attus.processos.persistence.processo;

import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Transactional
public class ProcessoJpaAdapter implements ProcessoRepositoryPort {

    private final ProcessoRepository repo;

    @Override
    @Transactional(readOnly = true)
    public Optional<Processo> findById(@NonNull Long id) {
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Processo> search(ProcessoFilter filter, Pageable page) {
        return repo.findAll(ProcessoSpecification.of(filter), page);
    }

    @Override
    public Processo save(@NonNull Processo processo) {
        return repo.saveAndFlush(processo);
    }

    @Override
    public void flush() {
        repo.flush();
    }
}
