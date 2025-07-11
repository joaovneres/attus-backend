package br.com.attus.processos.application.controller;

import br.com.attus.processos.application.dto.AcaoDto;
import br.com.attus.processos.application.service.AcaoService;
import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
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
        path = "/processos/{procId}/acoes",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AcaoController {

    private final AcaoService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> criar(@PathVariable @Positive Long procId,
                                      @RequestBody @Valid AcaoDto.CreateRequest body) {

        service.criar(new AcaoDto.CreateRequest(
                procId,
                body.tipo(),
                body.dataRegistro(),
                body.descricao()));

        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<?> pesquisar(
            @PathVariable @Positive Long procId,
            @RequestParam(required = false) TipoAcao tipo,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Pageable pageable) {

        return ResponseEntity.ok(
                service.pesquisar(procId, tipo, dataInicio, dataFim, pageable));
    }
}
