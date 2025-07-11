package br.com.attus.processos.nucleo.application.port.out.parte;

import br.com.attus.processos.nucleo.dominio.enums.TipoParte;

public record ParteFilter(
        Long      processoId,
        TipoParte tipo,
        String    cpfCnpj,
        String    nome
) {
    public boolean isEmpty() {
        return processoId == null &&
                tipo        == null &&
                (cpfCnpj == null || cpfCnpj.isBlank()) &&
                (nome    == null || nome.isBlank());
    }
}
