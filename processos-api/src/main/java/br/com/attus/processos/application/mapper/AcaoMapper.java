package br.com.attus.processos.application.mapper;

import br.com.attus.processos.application.dto.AcaoDto;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AcaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "processo", expression = "java(new Processo(dto.processoId()))")
    Acao toEntity(AcaoDto.CreateRequest dto);

    @Mapping(target = "processoId", source = "processo.id")
    AcaoDto.Response toDto(Acao acao);
}
