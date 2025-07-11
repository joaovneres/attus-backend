package br.com.attus.processos.application.mapper;

import br.com.attus.processos.application.dto.ParteDto;
import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.vo.Contato;
import br.com.attus.processos.nucleo.dominio.vo.DocumentoFiscal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ParteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "processo", expression = "java(toProcesso(dto.processoId()))")
    @Mapping(target = "documentoFiscal", expression = "java(toDocumento(dto.cpfCnpj()))")
    @Mapping(target = "contato", expression = "java(toContato(dto.email(), dto.telefone()))")
    Parte toEntity(ParteDto.CreateRequest dto);

    @Mapping(target = "processoId", source = "processo.id")
    @Mapping(target = "cpfCnpj", source = "documentoFiscal.valor")
    @Mapping(target = "email", source = "contato.email")
    @Mapping(target = "telefone", source = "contato.telefone")
    ParteDto.Response toDto(Parte parte);

    default Processo toProcesso(Long id) {
        return id == null ? null : new Processo(id);
    }

    default DocumentoFiscal toDocumento(String v) {
        return v == null ? null : new DocumentoFiscal(v);
    }

    default Contato toContato(String email, String tel) {
        return (email == null && tel == null) ? null : new Contato(email, tel);
    }
}
