package br.com.attus.processos.api.adapter.out.persistence.parte;

import br.com.attus.processos.nucleo.application.port.out.parte.ParteFilter;
import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
class ParteSpecification {
    Specification<Parte> toSpec(ParteFilter f) {

        Specification<Parte> spec = null;
        spec = append(spec, byProcesso(f.processoId()));
        spec = append(spec, byTipo(f.tipo()));
        spec = append(spec, byCpfCnpj(f.cpfCnpj()));
        spec = append(spec, byNome(f.nome()));
        return spec;
    }

    private Specification<Parte> byProcesso(Long id) {
        return (r,q,c) -> id == null ? null : c.equal(r.get("processo").get("id"), id);
    }
    private Specification<Parte> byTipo(TipoParte tipo) {
        return (r,q,c) -> tipo == null ? null : c.equal(r.get("tipo"), tipo);
    }
    private Specification<Parte> byCpfCnpj(String v) {
        return (r,q,c) -> v == null || v.isBlank() ? null
                : c.equal(r.get("documentoFiscal").get("valor"), v);
    }
    private Specification<Parte> byNome(String n) {
        return (r,q,c) -> n == null || n.isBlank() ? null
                : c.like(c.lower(r.get("nome")), "%"+n.toLowerCase()+"%");
    }
    private Specification<Parte> append(Specification<Parte> base, Specification<Parte> extra) {
        return extra == null ? base : (base == null ? extra : base.and(extra));
    }
}
