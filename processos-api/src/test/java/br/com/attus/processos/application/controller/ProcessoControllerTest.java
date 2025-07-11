package br.com.attus.processos.application.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.attus.processos.application.dto.ProcessoDto;
import br.com.attus.processos.application.service.ProcessoService;
import br.com.attus.processos.nucleo.application.port.out.processo.ProcessoFilter;
import br.com.attus.processos.nucleo.dominio.enums.StatusProcesso;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.LocalDate;
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

@WebMvcTest(ProcessoController.class)
class ProcessoControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ProcessoService service;

    @Nested
    @DisplayName("POST /processos")
    class Criar {

        @Test
        void deveCriarProcesso() throws Exception {
            var body = new ProcessoDto.CreateRequest("0001", LocalDate.now(), "desc");

            willDoNothing().given(service).criar(any());

            mvc.perform(post("/processos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(body)))
                    .andExpect(status().isAccepted());

            ArgumentCaptor<ProcessoDto.CreateRequest> captor =
                    ArgumentCaptor.forClass(ProcessoDto.CreateRequest.class);
            then(service).should().criar(captor.capture());
            assert captor.getValue().numero().equals("0001");
        }

        @Test
        void deveValidarCorpo() throws Exception {
            mvc.perform(post("/processos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());

            then(service).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("GET /processos/{id}")
    class Buscar {

        @Test
        void deveBuscarProcesso() throws Exception {
            given(service.buscar(1L))
                    .willReturn(new ProcessoDto.Response(
                            1L, "0002", LocalDate.now(), "ok", StatusProcesso.ATIVO, Instant.now(), Instant.now()));

            mvc.perform(get("/processos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        }
    }

    @Nested
    @DisplayName("GET /processos")
    class Pesquisar {

        @Test
        void deveFiltrarPorStatus() throws Exception {
            given(service.pesquisar(any(ProcessoFilter.class), any(Pageable.class)))
                    .willReturn(new PageImpl<>(List.of(
                            new ProcessoDto.Response(
                                    2L, "0003", LocalDate.now(), "x", StatusProcesso.SUSPENSO, Instant.now(), Instant.now()))));

            mvc.perform(get("/processos")
                            .param("status", "SUSPENSO"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].status").value("SUSPENSO"));
        }
    }

    @Nested
    @DisplayName("PUT /processos/{id}")
    class Atualizar {

        @Test
        void deveAtualizar() throws Exception {
            var body = new ProcessoDto.UpdateRequest("desc nova");

            mvc.perform(put("/processos/5")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(body)))
                    .andExpect(status().isNoContent());

            then(service).should().atualizar(eq(5L), any(ProcessoDto.UpdateRequest.class));
        }
    }

    @Nested
    @DisplayName("PATCH /processos/{id}/status")
    class MudarStatus {

        @Test
        void deveMudarStatus() throws Exception {
            mvc.perform(patch("/processos/7/status")
                            .param("status", "ARQUIVADO"))
                    .andExpect(status().isNoContent());

            then(service).should().mudarStatus(7L, StatusProcesso.ARQUIVADO);
        }
    }
}