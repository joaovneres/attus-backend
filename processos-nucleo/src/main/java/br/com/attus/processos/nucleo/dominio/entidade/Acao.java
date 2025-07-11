package br.com.attus.processos.nucleo.dominio.entidade;

import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "acao")
@SuppressWarnings("java:S2160")
public class Acao extends EntidadeBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Processo processo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TipoAcao tipo;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    @Column(columnDefinition = "text")
    private String descricao;

    public Acao(Processo processo,
                TipoAcao tipo,
                LocalDate dataRegistro,
                String descricao) {

        this.processo = Objects.requireNonNull(processo, "processo não pode ser nulo");
        this.tipo = Objects.requireNonNull(tipo, "tipo não pode ser nulo");
        this.dataRegistro = Objects.requireNonNull(dataRegistro, "dataRegistro não pode ser nula");

        if (dataRegistro.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("dataRegistro não pode estar no futuro");
        }
        this.descricao = descricao;
    }

    public void setProcesso(Processo processo) {
        this.processo = Objects.requireNonNull(processo, "processo não pode ser nulo");
    }

    public void alterarDescricao(String novaDescricao) {
        this.descricao = novaDescricao;
    }
}
