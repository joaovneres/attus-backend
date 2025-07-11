package br.com.attus.processos.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.attus.processos.application.dto.AcaoDto;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class AcaoMapperTest {

    private final AcaoMapper mapper = Mappers.getMapper(AcaoMapper.class);

    @Test
    void toEntity() {
        AcaoDto.CreateRequest dto = new AcaoDto.CreateRequest(
                99L, TipoAcao.SENTENCA, LocalDate.of(2025, 7, 11), "descrição");

        Acao entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getProcesso()).isNotNull();
        assertThat(entity.getProcesso().getId()).isEqualTo(99L);
        assertThat(entity.getTipo()).isEqualTo(TipoAcao.SENTENCA);
        assertThat(entity.getDataRegistro()).isEqualTo(LocalDate.of(2025, 7, 11));
        assertThat(entity.getDescricao()).isEqualTo("descrição");
    }

    @Test
    void toDto() {
        Processo processo = new Processo(10L);

        Acao entity = new Acao(processo, TipoAcao.AUDIENCIA, LocalDate.of(2024, 12, 31), "ok");
        AcaoDto.Response dto = mapper.toDto(entity);

        assertThat(dto.processoId()).isEqualTo(10L);
        assertThat(dto.tipo()).isEqualTo(TipoAcao.AUDIENCIA);
        assertThat(dto.dataRegistro()).isEqualTo(LocalDate.of(2024, 12, 31));
        assertThat(dto.descricao()).isEqualTo("ok");
    }
}