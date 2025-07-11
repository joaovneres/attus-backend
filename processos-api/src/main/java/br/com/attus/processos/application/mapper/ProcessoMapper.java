package br.com.attus.processos.application.mapper;

import br.com.attus.processos.application.dto.ProcessoDto;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProcessoMapper {

    @Mapping(target = "id", ignore = true)
    Processo toEntity(ProcessoDto.CreateRequest dto);

    ProcessoDto.Response toDto(Processo entity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(ProcessoDto.UpdateRequest dto, @MappingTarget Processo target);
}