package br.com.attus.processos.api.adapter.in.web.controller;

import br.com.attus.processos.api.adapter.in.web.dto.ProcessoDto;
import br.com.attus.processos.api.application.service.ProcessoService;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/processos")
public record ProcessoController(ProcessoService service) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProcessoDto.Response criar(@RequestBody @Valid ProcessoDto.CreateRequest body) {
        return service.criar(body);
    }

    @GetMapping("/{id}")
    public ProcessoDto.Response buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping
    public Page<ProcessoDto.Response> pesquisar(
            @RequestParam(required = false) StatusProcesso status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) String cpfCnpj,
            Pageable pageable) {

        return service.pesquisar(new ProcessoFilter(status, dataInicio, dataFim, cpfCnpj), pageable);
    }

    @PutMapping("/{id}")
    public ProcessoDto.Response atualizar(@PathVariable Long id,
                                          @RequestBody @Valid ProcessoDto.UpdateRequest body) {
        return service.atualizar(id, body);
    }

    @PatchMapping("/{id}/status")
    public ProcessoDto.Response mudarStatus(@PathVariable Long id,
                                            @RequestParam StatusProcesso status) {
        return service.mudarStatus(id, status);
    }
}
