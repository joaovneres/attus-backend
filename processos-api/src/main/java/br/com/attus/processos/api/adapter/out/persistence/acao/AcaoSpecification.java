package br.com.attus.processos.api.adapter.out.persistence.acao;

import br.com.attus.processos.nucleo.application.port.out.acao.AcaoFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
class AcaoSpecification {

    Specification<Acao> toSpec(AcaoFilter f) {

        Specification<Acao> spec = null;
        spec = append(spec, byProcesso(f.processoId()));
        spec = append(spec, byTipo(f.tipo()));
        spec = append(spec, betweenDates(f.dataInicio(), f.dataFim()));
        return spec;
    }

    private Specification<Acao> byProcesso(Long id) {
        return (r,q,c) -> id == null ? null : c.equal(r.get("processo").get("id"), id);
    }
    private Specification<Acao> byTipo(TipoAcao tipo) {
        return (r,q,c) -> tipo == null ? null : c.equal(r.get("tipo"), tipo);
    }
    private Specification<Acao> betweenDates(LocalDate ini, LocalDate fim) {
        return (r,q,c) -> {
            if (ini == null && fim == null) return null;
            if (ini != null && fim != null) return c.between(r.get("dataRegistro"), ini, fim);
            return ini != null ? c.greaterThanOrEqualTo(r.get("dataRegistro"), ini)
                    : c.lessThanOrEqualTo(r.get("dataRegistro"), fim);
        };
    }
    private Specification<Acao> append(Specification<Acao> base, Specification<Acao> extra) {
        return extra == null ? base : (base == null ? extra : base.and(extra));
    }
}
