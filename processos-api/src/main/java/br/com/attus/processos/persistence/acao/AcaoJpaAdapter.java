package br.com.attus.processos.persistence.acao;

import br.com.attus.processos.nucleo.application.port.out.acao.AcaoFilter;
import br.com.attus.processos.nucleo.application.port.out.acao.AcaoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
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
public class AcaoJpaAdapter implements AcaoRepositoryPort {

    private final AcaoRepository repo;

    @Override
    @Transactional(readOnly = true)
    public Optional<Acao> findById(@NonNull Long id) {
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Acao> search(AcaoFilter filter, Pageable page) {
        return repo.findAll(AcaoSpecification.of(filter), page);
    }

    @Override
    public Acao save(@NonNull Acao acao) {
        return repo.saveAndFlush(acao);
    }

    @Override
    public void flush() {
        repo.flush();
    }
}

