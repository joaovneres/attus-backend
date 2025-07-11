package br.com.attus.processos.application.service;

import br.com.attus.processos.application.dto.ParteDto;
import br.com.attus.processos.application.mapper.ParteMapper;
import br.com.attus.processos.nucleo.application.port.out.parte.ParteCommandPublisherPort;
import br.com.attus.processos.nucleo.application.port.out.parte.ParteFilter;
import br.com.attus.processos.nucleo.application.port.out.parte.ParteRepositoryPort;
import br.com.attus.processos.nucleo.dominio.event.ParteCommand;
import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
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
public class ParteService {

    private final ParteRepositoryPort repo;
    private final ParteCommandPublisherPort publisher;
    private final ParteMapper mapper;

    @Transactional
    public void criar(@Valid @NonNull ParteDto.CreateRequest dto) {
        log.info("Publicando comando de criação de Parte para processo {}", dto.processoId());
        publisher.publish(new ParteCommand.Create(
                dto.processoId(),
                dto.nome(),
                dto.cpfCnpj(),
                dto.tipo(),
                dto.email(),
                dto.telefone()));
    }

    @Transactional
    public void atualizarContato(@NonNull Long parteId,
                                 @Valid @NonNull ParteDto.UpdateContatoRequest dto) {
        log.info("Publicando comando de atualização de contato da Parte {}", parteId);
        publisher.publish(new ParteCommand.UpdateContato(
                parteId, dto.email(), dto.telefone()));
    }

    @Transactional(readOnly = true)
    public ParteDto.Response buscar(@NonNull Long id) {
        return repo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Parte não encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<ParteDto.Response> pesquisar(Long processoId,
                                             TipoParte tipo,
                                             String cpfCnpj,
                                             String nome,
                                             Pageable pageable) {

        ParteFilter filtro = new ParteFilter(processoId, tipo, cpfCnpj, nome);
        log.debug("Pesquisando partes com filtro {}", filtro);

        return repo.search(filtro, pageable)
                .map(mapper::toDto);
    }
}