package br.com.attus.processos.persistence.processo;

import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import java.time.LocalDate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.JoinType;

@UtilityClass
public class ProcessoSpecification {

    public static Specification<Processo> of(ProcessoFilter f) {
        return Specification
                .<Processo>where(byStatus(f.status()))
                .and(betweenDates(f.dataInicio(), f.dataFim()))
                .and(byCpfCnpj(f.cpfCnpj()));
    }

    private static Specification<Processo> byStatus(StatusProcesso status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    private static Specification<Processo> betweenDates(LocalDate ini, LocalDate fim) {
        return (root, query, cb) -> {
            if (ini == null && fim == null) return null;
            if (ini != null && fim != null)
                return cb.between(root.get("dataAbertura"), ini, fim);

            return ini != null
                    ? cb.greaterThanOrEqualTo(root.get("dataAbertura"), ini)
                    : cb.lessThanOrEqualTo(root.get("dataAbertura"), fim);
        };
    }

    private static Specification<Processo> byCpfCnpj(String cpfCnpj) {
        return (root, query, cb) ->
                isBlank(cpfCnpj) ? null
                        : cb.equal(
                        root.join("partes", JoinType.LEFT)
                                .get("documentoFiscal").get("valor"),
                        cpfCnpj);
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
