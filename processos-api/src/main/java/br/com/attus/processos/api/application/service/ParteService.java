/* ParteService.java */
package br.com.attus.processos.api.application.service;

import br.com.attus.processos.api.adapter.in.web.dto.ParteDto;
import br.com.attus.processos.api.adapter.in.web.mapper.ParteMapper;
import br.com.attus.processos.nucleo.application.port.out.parte.ParteCommandPublisherPort;
import br.com.attus.processos.nucleo.application.port.out.parte.ParteFilter;
import br.com.attus.processos.nucleo.application.port.out.parte.ParteRepositoryPort;
import br.com.attus.processos.nucleo.dominio.event.ParteCommand;
import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParteService {

    private final ParteRepositoryPort repo;
    private final ParteCommandPublisherPort publisher;
    private final ParteMapper                  mapper;

    /* ------- Commands (assíncronos) ------- */

    public void criar(ParteDto.CreateRequest dto) {
        publisher.publish(new ParteCommand.Create(
                dto.processoId(), dto.nome(), dto.cpfCnpj(), dto.tipo(),
                dto.email(), dto.telefone()));
    }

    public void atualizarContato(Long parteId, ParteDto.UpdateContatoRequest dto) {
        publisher.publish(new ParteCommand.UpdateContato(
                parteId, dto.email(), dto.telefone()));
    }

    /* ------- Queries (síncronas) ------- */

    public ParteDto.Response buscar(Long id) {
        return repo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Parte não encontrada"));
    }

    public Page<ParteDto.Response> pesquisar(Long processoId,
                                             TipoParte tipo,
                                             String cpfCnpj,
                                             String nome,
                                             Pageable pg) {
        ParteFilter f = new ParteFilter(processoId, tipo, cpfCnpj, nome);
        return repo.search(f, pg).map(mapper::toDto);
    }
}
