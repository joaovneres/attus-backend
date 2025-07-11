package br.com.attus.processos.nucleo.application.port.out.acao;

import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AcaoRepositoryPort {

    Optional<Acao> findById(Long id);

    Acao save(Acao acao);

    Page<Acao> search(AcaoFilter filter, Pageable pageable);

    void flush();
}
