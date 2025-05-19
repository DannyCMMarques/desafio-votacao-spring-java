package com.crud.demo.web.rest;

import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.crud.demo.domain.enums.DuracaoSessaoEnum;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.exceptions.sessao.SessaoNaoCadastradaException;
import com.crud.demo.service.SessaoService;
import com.crud.demo.service.dto.sessao.SessaoRequestDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SessaoController.class)
@DisplayName("SessaoController – testes de endpoints REST")
class SessaoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private SessaoService sessaoService;

    private SessaoRequestDTO request;
    private SessaoResponseDTO response;

    @BeforeEach
    void setUp() {
        request = new SessaoRequestDTO();
        request.setIdPauta(1L);
        request.setDuracao(10.0);
        request.setUnidade(DuracaoSessaoEnum.MIN);

        response = SessaoResponseDTO.builder()
                .id(1L)
                .status(StatusSessaoEnum.NAO_INICIADA)
                .duracao(10.0)
                .build();
    }

    @Test
    @DisplayName(" Deve cadastrar sessão com Location com Sucesso")
    void deveCadastrarComSucesso() throws Exception {
        when(sessaoService.criarSessao(refEq(request)))
                .thenReturn(response);
        mockMvc.perform(post("/api/v1/sessao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION,
                        endsWith("/api/v1/sessao/1")))
                .andExpect(jsonPath("$.id", is(1)));
        verify(sessaoService).criarSessao(refEq(request));
    }

    @Test
    @DisplayName("Deve listar sessões paginadas")
    void deveListarComSucesso() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(sessaoService.listarSessoes(0, 10, "id", "asc"))
                .thenReturn(new PageImpl<>(List.of(response), pageable, 1));

        mockMvc.perform(get("/api/v1/sessao")
                .param("page", "0").param("size", "10")
                .param("sortBy", "id").param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)));

        verify(sessaoService).listarSessoes(0, 10, "id", "asc");
    }

    @Test
    @DisplayName(" Deve retornar sessão existente")
    void deveBuscarPorIdComSucesso() throws Exception {
        when(sessaoService.buscarPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/sessao/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(sessaoService).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve retornar quando sessão não encontrada")
    void deveRetornar404QuandoNaoEncontrada() throws Exception {
        when(sessaoService.buscarPorId(99L))
                .thenThrow(new SessaoNaoCadastradaException());

        mockMvc.perform(get("/api/v1/sessao/99"))
                .andExpect(status().isNotFound());

        verify(sessaoService).buscarPorId(99L);
    }

    @Test
    @DisplayName("Deve atualizar sessão existente")
    void deveAtualizarComSucesso() throws Exception {
        SessaoResponseDTO atualizado = SessaoResponseDTO.builder()
                .id(1L)
                .status(StatusSessaoEnum.EM_ANDAMENTO)
                .duracao(15.0)
                .build();

        when(sessaoService.atualizarSessao(eq(1L), refEq(request)))
                .thenReturn(atualizado);

        mockMvc.perform(put("/api/v1/sessao/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("EM_ANDAMENTO")));

        verify(sessaoService).atualizarSessao(eq(1L), refEq(request));
    }

    @Test
    @DisplayName("Deve deletar sessão existente")
    void deveDeletarComSucesso() throws Exception {
        mockMvc.perform(delete("/api/v1/sessao/1"))
                .andExpect(status().isNoContent());

        verify(sessaoService).deletarSessao(1L);
    }

}
