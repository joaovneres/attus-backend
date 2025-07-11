package br.com.attus.processos.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import br.com.attus.processos.application.dto.ParteDto;
import br.com.attus.processos.application.mapper.ParteMapper;
import br.com.attus.processos.nucleo.application.port.out.parte.ParteCommandPublisherPort;
import br.com.attus.processos.nucleo.application.port.out.parte.ParteFilter;
import br.com.attus.processos.nucleo.application.port.out.parte.ParteRepositoryPort;
import br.com.attus.processos.nucleo.dominio.entidade.Parte;
import br.com.attus.processos.nucleo.dominio.event.ParteCommand;
import br.com.attus.processos.nucleo.dominio.enums.TipoParte;

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
class ParteServiceTest {

    @Mock ParteRepositoryPort repo;
    @Mock ParteCommandPublisherPort publisher;

    ParteMapper mapper = Mappers.getMapper(ParteMapper.class);
    ParteService service;

    @BeforeEach
    void setUp() {
        service = new ParteService(repo, publisher, mapper);
    }

    @Test
    void criar() {
        var dto = new ParteDto.CreateRequest(
                5L, "Ana", "12345678901", TipoParte.AUTOR,
                "ana@mail.com", "1199");

        service.criar(dto);

        ArgumentCaptor<ParteCommand.Create> captor = ArgumentCaptor.forClass(ParteCommand.Create.class);
        then(publisher).should().publish(captor.capture());

        var cmd = captor.getValue();
        assertThat(cmd.processoId()).isEqualTo(5L);
        assertThat(cmd.nome()).isEqualTo("Ana");
        then(repo).shouldHaveNoInteractions();
    }

    @Test
    void atualizarContato() {
        var dto = new ParteDto.UpdateContatoRequest("bob@mail.com", "1198");

        service.atualizarContato(9L, dto);

        ArgumentCaptor<ParteCommand.UpdateContato> captor = ArgumentCaptor.forClass(ParteCommand.UpdateContato.class);
        then(publisher).should().publish(captor.capture());

        var cmd = captor.getValue();
        assertThat(cmd.parteId()).isEqualTo(9L);
        assertThat(cmd.telefone()).isEqualTo("1198");
    }

    @Nested
    class Buscar {

        @Test
        void deveRetornarParteQuandoExiste() {
            Parte entidade = mapper.toEntity(new ParteDto.CreateRequest(
                    1L, "Ana", "123", TipoParte.AUTOR, null, null));
            entidade.setId(11L);

            given(repo.findById(11L)).willReturn(Optional.of(entidade));

            var res = service.buscar(11L);

            assertThat(res.id()).isEqualTo(11L);
        }

        @Test
        void deveLancarExcecaoQuandoNaoEncontrada() {
            given(repo.findById(99L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> service.buscar(99L))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("Parte não encontrada");
        }
    }

    @Nested
    class Pesquisar {

        @Test
        void deveMontarFiltroEMapearResultados() {
            Parte entidade = mapper.toEntity(new ParteDto.CreateRequest(
                    2L, "Bob", "987", TipoParte.REU, null, null));
            given(repo.search(any(ParteFilter.class), any(Pageable.class)))
                    .willReturn(new PageImpl<>(List.of(entidade)));

            var page = service.pesquisar(2L, TipoParte.REU,
                    "987", "Bob", PageRequest.of(0, 5));

            assertThat(page.getTotalElements()).isEqualTo(1);
            assertThat(page.getContent().get(0).nome()).isEqualTo("Bob");

            ArgumentCaptor<ParteFilter> captor = ArgumentCaptor.forClass(ParteFilter.class);
            then(repo).should().search(captor.capture(), any(Pageable.class));
            var f = captor.getValue();
            assertThat(f.processoId()).isEqualTo(2L);
            assertThat(f.nome()).isEqualTo("Bob");
        }
    }
}