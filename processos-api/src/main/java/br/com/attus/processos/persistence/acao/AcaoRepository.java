package br.com.attus.processos.persistence.acao;

import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AcaoRepository
        extends JpaRepository<Acao, Long>,
        JpaSpecificationExecutor<Acao> { }
