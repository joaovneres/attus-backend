package br.com.attus.processos.persistence.parte;

import br.com.attus.processos.nucleo.application.port.out.parte.ParteFilter;
import br.com.attus.processos.nucleo.application.port.out.parte.ParteRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Parte;
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
public class ParteJpaAdapter implements ParteRepositoryPort {

    private final ParteRepository repo;

    @Override
    @Transactional(readOnly = true)
    public Optional<Parte> findById(@NonNull Long id) {
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Parte> search(ParteFilter filter, Pageable page) {
        return repo.findAll(ParteSpecification.of(filter), page);
    }

    @Override
    public Parte save(@NonNull Parte parte) {
        return repo.saveAndFlush(parte);
    }

    @Override
    public void flush() {
        repo.flush();
    }
}
