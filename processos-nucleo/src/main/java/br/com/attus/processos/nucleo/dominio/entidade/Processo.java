package br.com.attus.processos.nucleo.dominio.entidade;

import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@SuppressWarnings("java:S2160")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "processo",
        uniqueConstraints = @UniqueConstraint(name = "uk_numero_processo",
                columnNames = "numero"))
@ToString(onlyExplicitlyIncluded = true)
public class Processo extends EntidadeBase {

    @ToString.Include
    @Column(nullable = false, length = 30)
    private String numero;

    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    @Column(columnDefinition = "text")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatusProcesso status = StatusProcesso.ATIVO;

    public Processo(String numero, LocalDate dataAbertura, String descricao) {
        this.numero = numero;
        this.dataAbertura = dataAbertura;
        this.descricao = descricao;
    }

    public void arquivar() {
        verificarTransicao("Processo já está arquivado",
                status == StatusProcesso.ARQUIVADO);
        status = StatusProcesso.ARQUIVADO;
    }

    public void suspender() {
        verificarTransicao("Apenas processos ATIVOS podem ser suspensos",
                status != StatusProcesso.ATIVO);
        status = StatusProcesso.SUSPENSO;
    }

    public void reabrir() {
        verificarTransicao("Apenas processos SUSPENSOS podem ser reabertos",
                status != StatusProcesso.SUSPENSO);
        status = StatusProcesso.ATIVO;
    }

    private void verificarTransicao(String msgErro, boolean condicaoErro) {
        if (condicaoErro) {
            throw new IllegalStateException(msgErro);
        }
    }
}
