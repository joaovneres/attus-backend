package br.com.attus.processos.consumidor.adapter.out.persistence;

import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAcaoRepository extends JpaRepository<Acao, Long> { }
