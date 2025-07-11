package br.com.attus.processos.persistence.acao;

import br.com.attus.processos.nucleo.application.port.out.acao.AcaoFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import java.time.LocalDate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class AcaoSpecification {

    public static Specification<Acao> of(AcaoFilter f) {

        return Specification
                .<Acao>where(byProcesso(f.processoId()))
                .and(byTipo(f.tipo()))
                .and(betweenDates(f.dataInicio(), f.dataFim()));
    }

    private static Specification<Acao> byProcesso(Long id) {
        return (root, query, cb) ->
                id == null ? null : cb.equal(root.get("processo").get("id"), id);
    }

    private static Specification<Acao> byTipo(TipoAcao tipo) {
        return (root, query, cb) ->
                tipo == null ? null : cb.equal(root.get("tipo"), tipo);
    }

    private static Specification<Acao> betweenDates(LocalDate ini, LocalDate fim) {
        return (root, query, cb) -> {
            if (ini == null && fim == null) return null;
            if (ini != null && fim != null)
                return cb.between(root.get("dataRegistro"), ini, fim);
            return ini != null
                    ? cb.greaterThanOrEqualTo(root.get("dataRegistro"), ini)
                    : cb.lessThanOrEqualTo(root.get("dataRegistro"), fim);
        };
    }
}
