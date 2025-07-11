package br.com.attus.processos.api.adapter.out.persistence.acao;

import br.com.attus.processos.nucleo.application.port.out.acao.AcaoFilter;
import br.com.attus.processos.nucleo.application.port.out.acao.AcaoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class AcaoJpaAdapter implements AcaoRepositoryPort {

    private final SpringDataAcaoRepository repo;
    private final AcaoSpecification spec;

    @Override public Optional<Acao> findById(Long id) { return repo.findById(id); }

    @Override public Acao save(Acao acao) { return repo.saveAndFlush(acao); }

    @Override public Page<Acao> search(AcaoFilter filter, Pageable pg) {
        return repo.findAll(spec.toSpec(filter), pg);
    }

    @Override public void flush() { repo.flush(); }
}
