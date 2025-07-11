package br.com.attus.processos.application.controller;

import br.com.attus.processos.application.dto.ProcessoDto;
import br.com.attus.processos.application.service.ProcessoService;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(
        path = "/processos",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProcessoController {

    private final ProcessoService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> criar(@RequestBody @Valid ProcessoDto.CreateRequest body) {
        service.criar(body);
        return ResponseEntity.accepted().build();
    }


    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> atualizar(@PathVariable @Positive Long id,
                                          @RequestBody @Valid ProcessoDto.UpdateRequest body) {

        service.atualizar(id, body);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> mudarStatus(@PathVariable @Positive Long id,
                                            @RequestParam StatusProcesso status) {

        service.mudarStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessoDto.Response> buscar(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @GetMapping
    public ResponseEntity<?> pesquisar(
            @RequestParam(required = false) StatusProcesso status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) String cpfCnpj,
            Pageable pageable) {

        ProcessoFilter filtro = new ProcessoFilter(status, dataInicio, dataFim, cpfCnpj);
        return ResponseEntity.ok(service.pesquisar(filtro, pageable));
    }
}