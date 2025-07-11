package br.com.attus.processos.consumidor.adapter.out.persistence;

import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataParteRepository extends JpaRepository<Parte, Long> { }
