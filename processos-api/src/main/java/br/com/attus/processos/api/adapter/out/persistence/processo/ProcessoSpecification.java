package br.com.attus.processos.api.adapter.out.persistence.processo;

import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import jakarta.persistence.criteria.JoinType;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
class ProcessoSpecification {

    Specification<Processo> toSpec(ProcessoFilter f) {

        Specification<Processo> spec = null;

        spec = append(spec, byStatus(f.status()));
        spec = append(spec, betweenDates(f.dataInicio(), f.dataFim()));
        spec = append(spec, byCpfCnpj(f.cpfCnpj()));

        return spec;
    }

    private Specification<Processo> byStatus(StatusProcesso status) {
        return (root, q, cb) ->
                Optional.ofNullable(status)
                        .map(s -> cb.equal(root.get("status"), s))
                        .orElse(null);
    }

    private Specification<Processo> betweenDates(LocalDate ini, LocalDate fim) {
        return (root, q, cb) -> {
            if (ini == null && fim == null) return null;
            if (ini != null && fim != null)
                return cb.between(root.get("dataAbertura"), ini, fim);
            return ini != null
                    ? cb.greaterThanOrEqualTo(root.get("dataAbertura"), ini)
                    : cb.lessThanOrEqualTo(root.get("dataAbertura"), fim);
        };
    }

    private Specification<Processo> byCpfCnpj(String cpfCnpj) {
        return (root, q, cb) -> {
            if (cpfCnpj == null || cpfCnpj.isBlank()) return null;
            return cb.equal(
                    root.join("partes", JoinType.LEFT)
                            .get("documentoFiscal")
                            .get("valor"),
                    cpfCnpj);
        };
    }

    private Specification<Processo> append(Specification<Processo> base,
                                           Specification<Processo> extra) {
        return extra == null ? base : (base == null ? extra : base.and(extra));
    }
}