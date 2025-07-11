package br.com.attus.processos.nucleo.application.port.out.processo;

import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;

import java.time.LocalDate;

public record ProcessoFilter(
        StatusProcesso status,
        LocalDate dataInicio,
        LocalDate dataFim,
        String cpfCnpj
) { }