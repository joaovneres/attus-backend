package br.com.attus.processos.consumer.persistence;

import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParteRepository extends JpaRepository<Parte, Long> { }
