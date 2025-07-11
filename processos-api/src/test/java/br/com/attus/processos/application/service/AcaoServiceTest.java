package br.com.attus.processos.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import br.com.attus.processos.application.dto.AcaoDto;
import br.com.attus.processos.application.mapper.AcaoMapper;
import br.com.attus.processos.nucleo.application.port.out.acao.AcaoCommandPublisherPort;
import br.com.attus.processos.nucleo.application.port.out.acao.AcaoFilter;
import br.com.attus.processos.nucleo.application.port.out.acao.AcaoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Acao;
import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import br.com.attus.processos.nucleo.dominio.event.AcaoCommand;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class AcaoServiceTest {

    @Mock AcaoRepositoryPort repo;
    @Mock AcaoCommandPublisherPort publisher;

    AcaoMapper mapper = Mappers.getMapper(AcaoMapper.class);
    AcaoService service;

    @BeforeEach
    void init() {
        service = new AcaoService(repo, publisher, mapper);
    }

    @Test
    void criar() {
        var dto = new AcaoDto.CreateRequest(44L, TipoAcao.PETICAO, LocalDate.of(2025, 7, 11), "desc");
        service.criar(dto);
        ArgumentCaptor<AcaoCommand.Create> captor = ArgumentCaptor.forClass(AcaoCommand.Create.class);
        then(publisher).should().publish(captor.capture());
        var cmd = captor.getValue();
        assertThat(cmd.processoId()).isEqualTo(44L);
        assertThat(cmd.tipo()).isEqualTo(TipoAcao.PETICAO);
        assertThat(cmd.descricao()).isEqualTo("desc");
        then(repo).shouldHaveNoInteractions();
    }

    @Nested
    class Pesquisar {

        @Mock Pageable pageable;

        @Test
        void devePesquisarEMapear() {
            Acao entidade = mapper.toEntity(new AcaoDto.CreateRequest(1L, TipoAcao.SENTENCA, LocalDate.of(2025, 1, 1), "texto"));
            entidade.setId(10L);

            given(repo.search(any(AcaoFilter.class), any(Pageable.class)))
                    .willReturn(new PageImpl<>(List.of(entidade)));

            var page = service.pesquisar(1L, TipoAcao.SENTENCA, null, null, PageRequest.of(0, 10));

            assertThat(page.getTotalElements()).isEqualTo(1);
            assertThat(page.getContent().get(0).id()).isEqualTo(10L);

            ArgumentCaptor<AcaoFilter> captor = ArgumentCaptor.forClass(AcaoFilter.class);
            then(repo).should().search(captor.capture(), any(Pageable.class));
            assertThat(captor.getValue().processoId()).isEqualTo(1L);
            assertThat(captor.getValue().tipo()).isEqualTo(TipoAcao.SENTENCA);
        }
    }
}