package br.com.attus.processos.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.attus.processos.application.dto.ParteDto;
import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ParteMapperTest {

    private final ParteMapper mapper = Mappers.getMapper(ParteMapper.class);

    @Test
    void toEntity() {
        ParteDto.CreateRequest dto = new ParteDto.CreateRequest(
                7L, "Maria", "12345678901", TipoParte.AUTOR,
                "maria@mail.com", "11998887777");

        Parte entity = mapper.toEntity(dto);

        assertThat(entity.getProcesso().getId()).isEqualTo(7L);
        assertThat(entity.getNome()).isEqualTo("Maria");
        assertThat(entity.getDocumentoFiscal().toString()).isEqualTo("12345678901");
        assertThat(entity.getTipo()).isEqualTo(TipoParte.AUTOR);
        assertThat(entity.getContato().getEmail()).isEqualTo("maria@mail.com");
        assertThat(entity.getContato().getTelefone()).isEqualTo("11998887777");
    }

    @Test
    void toDto() {
        Parte p = mapper.toEntity(new ParteDto.CreateRequest(
                5L, "Dr. João", "98765432100", TipoParte.ADVOGADO,
                null, null));
        p.setId(55L);

        ParteDto.Response dto = mapper.toDto(p);

        assertThat(dto.id()).isEqualTo(55L);
        assertThat(dto.processoId()).isEqualTo(5L);
        assertThat(dto.nome()).isEqualTo("Dr. João");
        assertThat(dto.cpfCnpj()).isEqualTo("98765432100");
        assertThat(dto.tipo()).isEqualTo(TipoParte.ADVOGADO);
        assertThat(dto.email()).isNull();
        assertThat(dto.telefone()).isNull();
    }

    @Test
    void optionalNulls() {
        ParteDto.CreateRequest dto = new ParteDto.CreateRequest(
                1L, "Ana", "11111111111", TipoParte.REU, null, null);

        Parte entity = mapper.toEntity(dto);

        assertThat(entity.getContato()).isNull();
        ParteDto.Response back = mapper.toDto(entity);
        assertThat(back.email()).isNull();
    }
}
