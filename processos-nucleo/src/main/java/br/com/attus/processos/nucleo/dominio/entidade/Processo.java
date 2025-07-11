package br.com.attus.processos.nucleo.dominio.entidade;

import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "processo",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_numero_processo",
                columnNames = "numero"))
@ToString(onlyExplicitlyIncluded = true)
@SuppressWarnings("java:S2160")
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

    public Processo(String numero,
                    LocalDate dataAbertura,
                    String descricao) {

        this.numero = Objects.requireNonNull(numero, "numero não pode ser nulo");
        this.dataAbertura = Objects.requireNonNull(dataAbertura, "dataAbertura não pode ser nula");
        if (dataAbertura.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("dataAbertura não pode estar no futuro");
        }
        this.descricao = descricao;
    }

    public Processo(Long id) {
        this.setId(id);
    }

    public void arquivar() {
        validarTransicao(StatusProcesso.ARQUIVADO,
                "Processo já está arquivado");
        this.status = StatusProcesso.ARQUIVADO;
    }

    public void suspender() {
        if (status != StatusProcesso.ATIVO) {
            throw new IllegalStateException("Apenas processos ATIVOS podem ser suspensos");
        }
        this.status = StatusProcesso.SUSPENSO;
    }

    public void reabrir() {
        if (status != StatusProcesso.SUSPENSO) {
            throw new IllegalStateException("Apenas processos SUSPENSOS podem ser reabertos");
        }
        this.status = StatusProcesso.ATIVO;
    }

    public void atualizarDescricao(String novaDescricao) {
        this.descricao = Objects.requireNonNull(novaDescricao, "novaDescricao não pode ser nula");
    }

    private void validarTransicao(StatusProcesso destino, String msgErro) {
        if (this.status == destino) throw new IllegalStateException(msgErro);
    }
}
