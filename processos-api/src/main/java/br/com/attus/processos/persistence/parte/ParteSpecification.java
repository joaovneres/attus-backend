package br.com.attus.processos.persistence.parte;

import br.com.attus.processos.nucleo.application.port.out.parte.ParteFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ParteSpecification {

    public static Specification<Parte> of(ParteFilter f) {
        return Specification
                .<Parte>where(byProcesso(f.processoId()))
                .and(byTipo(f.tipo()))
                .and(byCpfCnpj(f.cpfCnpj()))
                .and(byNome(f.nome()));
    }

    private static Specification<Parte> byProcesso(Long id) {
        return (root, query, cb) ->
                id == null ? null : cb.equal(root.get("processo").get("id"), id);
    }

    private static Specification<Parte> byTipo(TipoParte tipo) {
        return (root, query, cb) ->
                tipo == null ? null : cb.equal(root.get("tipo"), tipo);
    }

    private static Specification<Parte> byCpfCnpj(String v) {
        return (root, query, cb) ->
                isBlank(v) ? null : cb.equal(root.get("documentoFiscal").get("valor"), v);
    }

    private static Specification<Parte> byNome(String n) {
        return (root, query, cb) ->
                isBlank(n) ? null
                        : cb.like(cb.lower(root.get("nome")), "%" + n.toLowerCase() + "%");
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}