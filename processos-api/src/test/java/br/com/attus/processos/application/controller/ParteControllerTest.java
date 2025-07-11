package br.com.attus.processos.application.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.attus.processos.application.dto.ParteDto;
import br.com.attus.processos.application.service.ParteService;
import br.com.attus.processos.nucleo.dominio.enums.TipoParte;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ParteController.class)
class ParteControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ParteService service;

    @Nested
    @DisplayName("POST /processos/{procId}/partes")
    class Criar {

        @Test
        void deveCriarParte() throws Exception {
            var body = new ParteDto.CreateRequest(null, "Ana", "12345678901",
                    TipoParte.AUTOR, "ana@mail.com", "11990001111");

            willDoNothing().given(service).criar(any());

            mvc.perform(post("/processos/8/partes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(body)))
                    .andExpect(status().isAccepted());

            ArgumentCaptor<ParteDto.CreateRequest> captor =
                    ArgumentCaptor.forClass(ParteDto.CreateRequest.class);
            then(service).should().criar(captor.capture());
            assert captor.getValue().processoId() == 8L;
        }
    }

    @Nested
    @DisplayName("PATCH /partes/{id}/contato")
    class AtualizarContato {

        @Test
        void deveAtualizarContato() throws Exception {
            var body = new ParteDto.UpdateContatoRequest("bob@mail.com", "11991112222");

            mvc.perform(patch("/partes/77/contato")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(body)))
                    .andExpect(status().isNoContent());

            then(service).should()
                    .atualizarContato(eq(77L), any(ParteDto.UpdateContatoRequest.class));
        }
    }

    @Nested
    @DisplayName("GET /partes/{id}")
    class Buscar {

        @Test
        void deveBuscarParte() throws Exception {
            given(service.buscar(10L))
                    .willReturn(new ParteDto.Response(
                            10L, 1L, "Bob", "98765432100", TipoParte.REU,
                            "bob@mail.com", "11988887777"));

            mvc.perform(get("/partes/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(10));
        }
    }

    @Nested
    @DisplayName("GET /processos/{procId}/partes")
    class Pesquisar {

        @Test
        void devePesquisarPartes() throws Exception {
            given(service.pesquisar(eq(5L), eq(TipoParte.ADVOGADO),
                    any(), any(), any(Pageable.class)))
                    .willReturn(new PageImpl<>(List.of(
                            new ParteDto.Response(1L, 5L, "Dr. Silva", "123", TipoParte.ADVOGADO,
                                    "dr@xyz.com", "111"))));

            mvc.perform(get("/processos/5/partes")
                            .param("tipo", "ADVOGADO"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].tipo").value("ADVOGADO"));
        }
    }
}