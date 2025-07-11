/* ParteController.java */
package br.com.attus.processos.api.adapter.in.web.controller;

import br.com.attus.processos.api.adapter.in.web.dto.ParteDto;
import br.com.attus.processos.api.application.service.ParteService;
import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public record ParteController(ParteService service) {

    /* ---------- Commands ---------- */

    @PostMapping("/processos/{procId}/partes")
    public void criar(@PathVariable Long procId,
                      @RequestBody @Valid ParteDto.CreateRequest body) {
        service.criar(new ParteDto.CreateRequest(
                procId, body.nome(), body.cpfCnpj(), body.tipo(),
                body.email(), body.telefone()));
    }

    @PatchMapping("/partes/{id}/contato")
    public void atualizarContato(@PathVariable Long id,
                                 @RequestBody @Valid ParteDto.UpdateContatoRequest body) {
        service.atualizarContato(id, body);
    }

    /* ---------- Queries ---------- */

    @GetMapping("/partes/{id}")
    public ParteDto.Response buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/processos/{procId}/partes")
    public Page<ParteDto.Response> pesquisar(
            @PathVariable Long procId,
            @RequestParam(required = false) TipoParte tipo,
            @RequestParam(required = false) String cpfCnpj,
            @RequestParam(required = false) String nome,
            Pageable pageable) {
        return service.pesquisar(procId, tipo, cpfCnpj, nome, pageable);
    }
}
