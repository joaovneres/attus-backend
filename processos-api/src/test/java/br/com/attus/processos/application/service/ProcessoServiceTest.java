package br.com.attus.processos.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import br.com.attus.processos.application.dto.ProcessoDto;
import br.com.attus.processos.application.mapper.ProcessoMapper;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoCommandPublisherPort;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Processo;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import br.com.attus.processos.nucleo.dominio.event.ProcessoCommand;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
class ProcessoServiceTest {

    @Mock ProcessoCommandPublisherPort publisher;
    @Mock ProcessoRepositoryPort repo;

    ProcessoMapper mapper = Mappers.getMapper(ProcessoMapper.class);
    ProcessoService service;

    @BeforeEach
    void init() {
        service = new ProcessoService(publisher, repo, mapper);
    }

    @Test
    void criar() {
        var dto = new ProcessoDto.CreateRequest("001", LocalDate.of(2025, 7, 11), "desc");

        service.criar(dto);

        ArgumentCaptor<ProcessoCommand.Create> captor = ArgumentCaptor.forClass(ProcessoCommand.Create.class);
        then(publisher).should().publish(captor.capture());

        var cmd = captor.getValue();
        assertThat(cmd.numero()).isEqualTo("001");
        assertThat(cmd.descricao()).isEqualTo("desc");
        then(repo).shouldHaveNoInteractions();
    }

    @Test
    void atualizar() {
        var dto = new ProcessoDto.UpdateRequest("nova");

        service.atualizar(3L, dto);

        ArgumentCaptor<ProcessoCommand.UpdateDesc> captor = ArgumentCaptor.forClass(ProcessoCommand.UpdateDesc.class);
        then(publisher).should().publish(captor.capture());

        assertThat(captor.getValue().id()).isEqualTo(3L);
        assertThat(captor.getValue().descricao()).isEqualTo("nova");
    }

    @Test
    void mudarStatus() {
        service.mudarStatus(4L, StatusProcesso.ARQUIVADO);

        ArgumentCaptor<ProcessoCommand.ChangeStatus> captor = ArgumentCaptor.forClass(ProcessoCommand.ChangeStatus.class);
        then(publisher).should().publish(captor.capture());

        assertThat(captor.getValue().id()).isEqualTo(4L);
        assertThat(captor.getValue().novoStatus()).isEqualTo(StatusProcesso.ARQUIVADO);
    }

    @Nested
    class Buscar {

        @Test
        void deveRetornarProcessoQuandoExiste() {
            Processo entity = mapper.toEntity(new ProcessoDto.CreateRequest("010", LocalDate.of(2025, 1, 1), "x"));
            entity.setId(10L);

            given(repo.findById(10L)).willReturn(Optional.of(entity));

            var res = service.buscar(10L);
            assertThat(res.id()).isEqualTo(10L);
        }

        @Test
        void deveLancarExcecaoQuandoNaoEncontrado() {
            given(repo.findById(99L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> service.buscar(99L))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("Processo não encontrado");
        }
    }

    @Nested
    class Pesquisar {

        @Test
        void deveMontarFiltroEMapearResultados() {
            Processo entity = mapper.toEntity(new ProcessoDto.CreateRequest("020", LocalDate.now(), "y"));
            entity.setId(20L);

            given(repo.search(any(ProcessoFilter.class), any(Pageable.class)))
                    .willReturn(new PageImpl<>(List.of(entity)));

            var page = service.pesquisar(
                    new ProcessoFilter(null, null, null, null),
                    PageRequest.of(0, 5));

            assertThat(page.getTotalElements()).isEqualTo(1);
            assertThat(page.getContent().get(0).id()).isEqualTo(20L);
            then(repo).should().search(any(ProcessoFilter.class), any(Pageable.class));
        }
    }
}