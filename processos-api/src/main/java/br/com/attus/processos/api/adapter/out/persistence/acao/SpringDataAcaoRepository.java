package br.com.attus.processos.api.adapter.out.persistence.acao;

import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface SpringDataAcaoRepository
        extends JpaRepository<Acao, Long>, JpaSpecificationExecutor<Acao> { }