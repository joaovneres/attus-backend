package br.com.attus.processos.nucleo.dominio.entidade;

import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import br.com.attus.processos.nucleo.dominio.vo.Contato;
import br.com.attus.processos.nucleo.dominio.vo.DocumentoFiscal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("java:S2160")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "parte",
        uniqueConstraints = @UniqueConstraint(name = "uk_cpf_cnpj_processo",
                columnNames = {"processo_id", "cpf_cnpj"}))
public class Parte extends EntidadeBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Processo processo;

    @Column(nullable = false)
    private String nome;

    @Embedded
    @AttributeOverride(name = "valor",
            column = @Column(name = "cpf_cnpj", length = 14, nullable = false))
    private DocumentoFiscal documentoFiscal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TipoParte tipo;

    @Embedded
    private Contato contato;

    public Parte(Processo processo,
                 String nome,
                 DocumentoFiscal documentoFiscal,
                 TipoParte tipo,
                 Contato contato) {
        this.processo = processo;
        this.nome = nome;
        this.documentoFiscal = documentoFiscal;
        this.tipo = tipo;
        this.contato = contato;
    }
}
