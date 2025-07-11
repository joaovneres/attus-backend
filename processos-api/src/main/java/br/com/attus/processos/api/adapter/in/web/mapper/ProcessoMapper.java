package br.com.attus.processos.api.adapter.in.web.mapper;

import br.com.attus.processos.api.adapter.in.web.dto.ProcessoDto;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProcessoMapper {

    Processo toEntity(ProcessoDto.CreateRequest dto);

    ProcessoDto.Response toDto(Processo entity);

    void update(ProcessoDto.UpdateRequest dto,
                @MappingTarget Processo target);
}
