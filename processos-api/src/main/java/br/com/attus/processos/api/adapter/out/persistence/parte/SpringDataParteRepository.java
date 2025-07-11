package br.com.attus.processos.api.adapter.out.persistence.parte;

import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface SpringDataParteRepository
        extends JpaRepository<Parte, Long>, JpaSpecificationExecutor<Parte> { }
