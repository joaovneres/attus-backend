package br.com.attus.processos.api.application.service;

import br.com.attus.processos.api.adapter.in.web.dto.ProcessoDto;
import br.com.attus.processos.api.adapter.in.web.mapper.ProcessoMapper;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProcessoService {

    private final ProcessoRepositoryPort repo;
    private final ProcessoMapper mapper;

    @Transactional
    public ProcessoDto.Response criar(ProcessoDto.CreateRequest dto) {
        Processo processo = mapper.toEntity(dto);
        repo.save(processo);
        return mapper.toDto(processo);
    }

    @Transactional(readOnly = true)
    public ProcessoDto.Response buscar(Long id) {
        Processo p = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));
        return mapper.toDto(p);
    }

    @Transactional(readOnly = true)
    public Page<ProcessoDto.Response> pesquisar(ProcessoFilter f, Pageable pg) {
        return repo.search(f, pg)
                .map(mapper::toDto);
    }

    @Transactional
    public ProcessoDto.Response atualizar(Long id, ProcessoDto.UpdateRequest dto) {
        Processo p = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));
        mapper.update(dto, p);
        repo.flush();
        return mapper.toDto(p);
    }

    @Transactional
    public ProcessoDto.Response mudarStatus(Long id, StatusProcesso novo) {
        Processo p = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));

        switch (novo) {
            case ARQUIVADO  -> p.arquivar();
            case SUSPENSO   -> p.suspender();
            case ATIVO      -> p.reabrir();
        }
        repo.flush();
        return mapper.toDto(p);
    }
}
