package br.com.attus.processos.nucleo.dominio.entidade;

import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@SuppressWarnings("java:S2160")
@Getter
@NoArgsConstructor
@Entity
@Table(name = "acao")
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

    public Acao(Processo processo, TipoAcao tipo, LocalDate dataRegistro, String descricao) {
        this.processo = processo;
        this.tipo = tipo;
        this.dataRegistro = dataRegistro;
        this.descricao = descricao;
    }
}
