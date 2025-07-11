package br.com.attus.processos.application.service;

import br.com.attus.processos.application.dto.ProcessoDto;
import br.com.attus.processos.application.mapper.ProcessoMapper;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoCommandPublisherPort;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import br.com.attus.processos.nucleo.dominio.event.ProcessoCommand;
import jakarta.validation.Valid;

import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProcessoService {

    private final ProcessoCommandPublisherPort publisher;
    private final ProcessoRepositoryPort repo;
    private final ProcessoMapper mapper;

    public void criar(@Valid @NonNull ProcessoDto.CreateRequest dto) {
        log.info("Publicando comando de criação de Processo {}", dto.numero());
        publisher.publish(new ProcessoCommand.Create(
                dto.numero(), dto.dataAbertura(), dto.descricao()));
    }

    public void atualizar(@NonNull Long id,
                          @Valid @NonNull ProcessoDto.UpdateRequest dto) {
        log.info("Publicando comando de atualização de descrição do Processo {}", id);
        publisher.publish(new ProcessoCommand.UpdateDesc(id, dto.descricao()));
    }

    public void mudarStatus(@NonNull Long id, @NonNull StatusProcesso novo) {
        log.info("Publicando comando de mudança de status do Processo {} → {}", id, novo);
        publisher.publish(new ProcessoCommand.ChangeStatus(id, novo));
    }

    @Transactional(readOnly = true)
    public ProcessoDto.Response buscar(@NonNull Long id) {
        Processo p = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Processo não encontrado"));
        return mapper.toDto(p);
    }

    @Transactional(readOnly = true)
    public Page<ProcessoDto.Response> pesquisar(ProcessoFilter filtro,
                                                Pageable pageable) {
        return repo.search(filtro, pageable)
                .map(mapper::toDto);
    }
}
