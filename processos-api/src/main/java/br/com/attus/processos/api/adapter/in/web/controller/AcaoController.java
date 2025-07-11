/* AcaoController.java */
package br.com.attus.processos.api.adapter.in.web.controller;

import br.com.attus.processos.api.adapter.in.web.dto.AcaoDto;
import br.com.attus.processos.api.application.service.AcaoService;
import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public record AcaoController(AcaoService service) {

    /* command */
    @PostMapping("/processos/{procId}/acoes")
    public void criar(@PathVariable Long procId,
                      @RequestBody @Valid AcaoDto.CreateRequest body) {
        service.criar(new AcaoDto.CreateRequest(
                procId, body.tipo(), body.dataRegistro(), body.descricao()));
    }

    /* queries */
    @GetMapping("/processos/{procId}/acoes")
    public Page<AcaoDto.Response> pesquisar(
            @PathVariable Long procId,
            @RequestParam(required = false) TipoAcao tipo,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Pageable pageable) {
        return service.pesquisar(procId, tipo, dataInicio, dataFim, pageable);
    }
}
