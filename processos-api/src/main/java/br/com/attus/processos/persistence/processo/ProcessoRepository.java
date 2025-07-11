package br.com.attus.processos.persistence.processo;

import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessoRepository
        extends JpaRepository<Processo, Long>,
        JpaSpecificationExecutor<Processo> { }
