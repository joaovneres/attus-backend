package br.com.attus.processos.application.service;

import br.com.attus.processos.application.dto.AcaoDto;
import br.com.attus.processos.application.mapper.AcaoMapper;
import br.com.attus.processos.nucleo.application.port.out.acao.AcaoCommandPublisherPort;
import br.com.attus.processos.nucleo.application.port.out.acao.AcaoFilter;
import br.com.attus.processos.nucleo.application.port.out.acao.AcaoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.event.AcaoCommand;
import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import jakarta.validation.Valid;
import java.time.LocalDate;
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
public class AcaoService {

    private final AcaoRepositoryPort repo;
    private final AcaoCommandPublisherPort publisher;
    private final AcaoMapper mapper;

    @Transactional
    public void criar(@Valid @NonNull AcaoDto.CreateRequest dto) {
        log.info("Publicando comando de criação de Ação para processo {}", dto.processoId());
        publisher.publish(new AcaoCommand.Create(
                dto.processoId(),
                dto.tipo(),
                dto.dataRegistro(),
                dto.descricao()));
    }

    @Transactional(readOnly = true)
    public Page<AcaoDto.Response> pesquisar(Long processoId,
                                            TipoAcao tipo,
                                            LocalDate ini,
                                            LocalDate fim,
                                            Pageable pageable) {

        var filtro = new AcaoFilter(processoId, tipo, ini, fim);
        log.debug("Pesquisando ações com filtro {}", filtro);

        return repo.search(filtro, pageable)
                .map(mapper::toDto);
    }
}
