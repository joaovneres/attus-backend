/* AcaoService.java */
package br.com.attus.processos.api.application.service;

import br.com.attus.processos.api.adapter.in.web.dto.AcaoDto;
import br.com.attus.processos.api.adapter.in.web.mapper.AcaoMapper;
import br.com.attus.processos.nucleo.application.port.out.acao.AcaoCommandPublisherPort;
import br.com.attus.processos.nucleo.application.port.out.acao.AcaoFilter;
import br.com.attus.processos.nucleo.application.port.out.acao.AcaoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.event.AcaoCommand;
import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AcaoService {

    private final AcaoRepositoryPort repo;
    private final AcaoCommandPublisherPort publisher;
    private final AcaoMapper               mapper;

    /* command */
    public void criar(AcaoDto.CreateRequest dto) {
        publisher.publish(new AcaoCommand.Create(
                dto.processoId(), dto.tipo(), dto.dataRegistro(), dto.descricao()));
    }

    /* queries */
    public Page<AcaoDto.Response> pesquisar(Long processoId,
                                            TipoAcao tipo,
                                            LocalDate ini,
                                            LocalDate fim,
                                            Pageable pg) {
        AcaoFilter f = new AcaoFilter(processoId, tipo, ini, fim);
        return repo.search(f, pg).map(mapper::toDto);
    }
}
