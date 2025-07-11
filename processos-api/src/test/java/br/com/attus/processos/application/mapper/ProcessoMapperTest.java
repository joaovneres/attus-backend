package br.com.attus.processos.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.attus.processos.application.dto.ProcessoDto;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ProcessoMapperTest {

    private final ProcessoMapper mapper = Mappers.getMapper(ProcessoMapper.class);

    @Test
    void toEntity() {
        ProcessoDto.CreateRequest dto = new ProcessoDto.CreateRequest("123", LocalDate.of(2025, 6, 1), "descrição");
        Processo entity = mapper.toEntity(dto);
        assertThat(entity.getNumero()).isEqualTo("123");
        assertThat(entity.getDataAbertura()).isEqualTo(LocalDate.of(2025, 6, 1));
        assertThat(entity.getDescricao()).isEqualTo("descrição");
        assertThat(entity.getStatus()).isEqualTo(StatusProcesso.ATIVO);
    }

    @Test
    void toDto() {
        Processo p = mapper.toEntity(new ProcessoDto.CreateRequest("456", LocalDate.of(2024, 12, 31), "x"));
        p.setId(7L);
        p.suspender();
        ProcessoDto.Response dto = mapper.toDto(p);
        assertThat(dto.id()).isEqualTo(7L);
        assertThat(dto.numero()).isEqualTo("456");
        assertThat(dto.status()).isEqualTo(StatusProcesso.SUSPENSO);
        assertThat(dto.dataAbertura()).isEqualTo(LocalDate.of(2024, 12, 31));
    }

    @Test
    void updatePartial() {
        Processo p = mapper.toEntity(new ProcessoDto.CreateRequest("789", LocalDate.now(), "antiga"));
        ProcessoDto.UpdateRequest dto = new ProcessoDto.UpdateRequest("nova desc");
        mapper.update(dto, p);
        assertThat(p.getDescricao()).isEqualTo("nova desc");
        assertThat(p.getNumero()).isEqualTo("789");
    }

    @Test
    void updateIgnoreNull() {
        Processo p = mapper.toEntity(new ProcessoDto.CreateRequest("900", LocalDate.now(), "antes"));
        mapper.update(new ProcessoDto.UpdateRequest(null), p);
        assertThat(p.getDescricao()).isEqualTo("antes");
    }
}