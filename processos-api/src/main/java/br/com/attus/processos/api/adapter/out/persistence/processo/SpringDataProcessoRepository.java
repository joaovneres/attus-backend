package br.com.attus.processos.api.adapter.out.persistence.processo;

import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface SpringDataProcessoRepository
        extends JpaRepository<Processo, Long>,
        JpaSpecificationExecutor<Processo> { }
