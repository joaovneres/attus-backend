package br.com.attus.processos.api.adapter.out.persistence.parte;

import br.com.attus.processos.nucleo.application.port.out.parte.ParteFilter;
import br.com.attus.processos.nucleo.application.port.out.parte.ParteRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class ParteJpaAdapter implements ParteRepositoryPort {

    private final SpringDataParteRepository repo;
    private final ParteSpecification spec;

    @Override public Optional<Parte> findById(Long id) { return repo.findById(id); }

    @Override public Parte save(Parte parte) { return repo.saveAndFlush(parte); }

    @Override public Page<Parte> search(ParteFilter filter, Pageable pg) {
        return repo.findAll(spec.toSpec(filter), pg);
    }

    @Override public void flush() { repo.flush(); }
}
