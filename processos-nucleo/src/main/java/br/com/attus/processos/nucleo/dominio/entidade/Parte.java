package br.com.attus.processos.nucleo.dominio.entidade;

import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import br.com.attus.processos.nucleo.dominio.vo.Contato;
import br.com.attus.processos.nucleo.dominio.vo.DocumentoFiscal;
import jakarta.persistence.*;

import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "parte",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_cpf_cnpj_processo",
                columnNames = {"processo_id", "cpf_cnpj"}))
@SuppressWarnings("java:S2160")
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

        this.processo = Objects.requireNonNull(processo, "processo não pode ser nulo");
        this.nome = Objects.requireNonNull(nome, "nome não pode ser nulo");
        this.documentoFiscal = Objects.requireNonNull(documentoFiscal, "documentoFiscal não pode ser nulo");
        this.tipo = Objects.requireNonNull(tipo, "tipo não pode ser nulo");
        this.contato = contato;
    }

    public void atualizarContato(Contato novoContato) {
        this.contato = novoContato;
    }
}
