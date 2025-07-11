package br.com.attus.processos.api.application.service;

import br.com.attus.processos.api.adapter.in.web.dto.ProcessoDto;
import br.com.attus.processos.api.adapter.in.web.mapper.ProcessoMapper;
import br.com.attus.processos.nucleo.application.port.out.ProcessoCommandPublisherPort;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import br.com.attus.processos.nucleo.dominio.event.ProcessoCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProcessoService {

    private final ProcessoCommandPublisherPort publisher;   // nova porta
    private final ProcessoRepositoryPort repo;              // lê para buscar
    private final ProcessoMapper mapper;

    public void criar(ProcessoDto.CreateRequest dto) {
        publisher.publish(new ProcessoCommand.Create(
                dto.numero(), dto.dataAbertura(), dto.descricao()));
    }

    public void atualizar(Long id, ProcessoDto.UpdateRequest dto) {
        publisher.publish(new ProcessoCommand.UpdateDesc(id, dto.descricao()));
    }

    public void mudarStatus(Long id, StatusProcesso novo) {
        publisher.publish(new ProcessoCommand.ChangeStatus(id, novo));
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
}
