/* ParteMapper.java */
package br.com.attus.processos.api.adapter.in.web.mapper;

import br.com.attus.processos.api.adapter.in.web.dto.ParteDto;
import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ParteMapper {

    @Mappings({
            @Mapping(target = "processo.id", source = "processoId"),
            @Mapping(target = "documentoFiscal.valor", source = "cpfCnpj"),
            @Mapping(target = "contato.email",   source = "email"),
            @Mapping(target = "contato.telefone",source = "telefone")
    })
    Parte toEntity(ParteDto.CreateRequest dto);

    @Mappings({
            @Mapping(target = "processoId", source = "processo.id"),
            @Mapping(target = "cpfCnpj",    source = "documentoFiscal.valor"),
            @Mapping(target = "email",      source = "contato.email"),
            @Mapping(target = "telefone",   source = "contato.telefone")
    })
    ParteDto.Response toDto(Parte parte);
}
