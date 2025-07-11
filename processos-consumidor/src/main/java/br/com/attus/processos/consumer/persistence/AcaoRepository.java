package br.com.attus.processos.consumer.persistence;

import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcaoRepository extends JpaRepository<Acao, Long> { }
