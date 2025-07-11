package br.com.attus.processos.nucleo.dominio.vo;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Contato {
    private String email;
    private String telefone;
    protected Contato() {}
    public Contato(String email, String telefone) { this.email = email; this.telefone = telefone; }
}
