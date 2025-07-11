package br.com.attus.processos.application.controller;

import br.com.attus.processos.application.dto.ParteDto;
import br.com.attus.processos.application.service.ParteService;
import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;


import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/partes")
public class ParteController {

    private final ParteService service;

    public ParteController(ParteService service) {
        this.service = service;
    }

    @PostMapping(path = "/processos/{procId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> criar(@PathVariable @Positive Long procId,
                                      @RequestBody @Valid ParteDto.CreateRequest body) {

        service.criar(new ParteDto.CreateRequest(
                procId,
                body.nome(),
                body.cpfCnpj(),
                body.tipo(),
                body.email(),
                body.telefone()));

        return ResponseEntity.accepted().build();
    }

    @PatchMapping(path = "/{id}/contato", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> atualizarContato(@PathVariable @Positive Long id,
                                                 @RequestBody @Valid ParteDto.UpdateContatoRequest body) {
        service.atualizarContato(id, body);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParteDto.Response> buscar(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @GetMapping("/processos/{procId}")
    public ResponseEntity<?> pesquisar(@PathVariable @Positive Long procId,
                                       @RequestParam(required = false) TipoParte tipo,
                                       @RequestParam(required = false) String cpfCnpj,
                                       @RequestParam(required = false) String nome,
                                       Pageable pageable) {

        return ResponseEntity.ok(
                service.pesquisar(procId, tipo, cpfCnpj, nome, pageable));
    }
}
