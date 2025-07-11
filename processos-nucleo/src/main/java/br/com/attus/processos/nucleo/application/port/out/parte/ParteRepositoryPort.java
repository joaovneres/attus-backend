package br.com.attus.processos.nucleo.application.port.out.parte;

import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParteRepositoryPort {
    Optional<Parte> findById(Long id);
    Parte save(Parte parte);
    Page<Parte> search(ParteFilter filter, Pageable pageable);
    void flush();
}
