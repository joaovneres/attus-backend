package br.com.attus.processos.nucleo.application.port.out.acao;

import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import java.time.LocalDate;

public record AcaoFilter(
        Long processoId,
        TipoAcao tipo,
        LocalDate dataInicio,
        LocalDate dataFim
) {
    public boolean isEmpty() {
        return processoId == null &&
                tipo        == null &&
                dataInicio  == null &&
                dataFim     == null;
    }
}
