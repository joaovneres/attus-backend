/* AcaoMapper.java */
package br.com.attus.processos.api.adapter.in.web.mapper;

import br.com.attus.processos.api.adapter.in.web.dto.AcaoDto;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AcaoMapper {

    @Mapping(target = "processo.id", source = "processoId")
    Acao toEntity(AcaoDto.CreateRequest dto);

    @Mapping(target = "processoId", source = "processo.id")
    AcaoDto.Response toDto(Acao acao);
}
