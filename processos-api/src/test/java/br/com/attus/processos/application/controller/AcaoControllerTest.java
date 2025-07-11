package br.com.attus.processos.application.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.attus.processos.application.dto.AcaoDto;
import br.com.attus.processos.application.service.AcaoService;
import br.com.attus.processos.nucleo.dominio.enums.TipoAcao;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

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

@WebMvcTest(AcaoController.class)
class AcaoControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    AcaoService service;

    @Nested
    @DisplayName("POST /processos/{procId}/acoes")
    class CriarAcao {

        @Test
        @DisplayName("Deve criar ação válida e retornar 202")
        void deveCriarAcao() throws Exception {
            var body = new AcaoDto.CreateRequest(
                    null, TipoAcao.PETICAO, LocalDate.now(), "descrição");

            willDoNothing().given(service).criar(any());

            mvc.perform(post("/processos/1/acoes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(body)))
                    .andExpect(status().isAccepted());

            ArgumentCaptor<AcaoDto.CreateRequest> captor = ArgumentCaptor.forClass(AcaoDto.CreateRequest.class);
            then(service).should().criar(captor.capture());
            assert captor.getValue().processoId() == 1L;
        }

        @Test
        @DisplayName("Deve rejeitar corpo inválido e retornar 400")
        void deveValidarCampos() throws Exception {
            mvc.perform(post("/processos/1/acoes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());

            then(service).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("GET /processos/{procId}/acoes")
    class PesquisarAcoes {

        @Test
        @DisplayName("Deve retornar lista paginada filtrando por tipo")
        void devePesquisar() throws Exception {
            given(service.pesquisar(eq(1L), eq(TipoAcao.AUDIENCIA),
                    any(), any(), org.mockito.ArgumentMatchers.any(Pageable.class)))
                    .willReturn(new PageImpl<>(java.util.List.of(
                            new AcaoDto.Response(1L, 1L, TipoAcao.AUDIENCIA,
                                    LocalDate.now(), "ok"))));

            mvc.perform(get("/processos/1/acoes")
                            .param("tipo", "AUDIENCIA"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].tipo").value("AUDIENCIA"));
        }
    }
}