package com.crud.demo.web.rest;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.crud.demo.domain.enums.VotoEnum;
import com.crud.demo.exceptions.sessao.SessaoJaFinalizadaException;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;
import com.crud.demo.service.dto.voto.VotoRequestDTO;
import com.crud.demo.service.dto.voto.VotoResponseDTO;
import com.crud.demo.service.impl.VotoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(VotacaoController.class)
@DisplayName("VotacaoController – testes de endpoint POST /api/v1/sessao/{id}/votar")
class VotacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private VotoServiceImpl votoService;

    private VotoRequestDTO votoReq;
    private VotoResponseDTO votoRes;
    private AssociadoResponseDTO associadoResponse;

    @BeforeEach
    void setUp() {
        votoReq = new VotoRequestDTO();
        votoReq.setAssociado(10L);
        votoReq.setVoto(true);
        associadoResponse = AssociadoResponseDTO.builder()
                .id(1L)
                .nome("João da Silva")
                .build();

        votoRes = VotoResponseDTO.builder()
                .id(1L)
                .voto(VotoEnum.SIM)
                .associado(associadoResponse)
                .build();

    }

    @Test
    @DisplayName(" Deve registrar voto")
    void deveVotarComSucesso() throws Exception {
        when(votoService.criarVoto(refEq(votoReq), eq(5L)))
                .thenReturn(votoRes);

        mockMvc.perform(post("/api/v1/sessao/5/votar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(votoReq)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION,
                        endsWith("/api/v1/sessao/1")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.voto", is(VotoEnum.SIM.name())));

        verify(votoService).criarVoto(refEq(votoReq), eq(5L));
    }

    @Test
    @DisplayName("Deve retornar erro quando corpo é inválido")
    void deveRetornar400ParaBodyInvalido() throws Exception {
        VotoRequestDTO invalido = new VotoRequestDTO();
        mockMvc.perform(post("/api/v1/sessao/5/votar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(votoService);
    }

    @Test
    @DisplayName(" Deve retornar 422 quando sessão já finalizada")
    void deveRetornar422QuandoSessaoFinalizada() throws Exception {
        when(votoService.criarVoto(refEq(votoReq), eq(5L)))
                .thenThrow(new SessaoJaFinalizadaException());

        mockMvc.perform(post("/api/v1/sessao/5/votar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(votoReq)))
                .andExpect(status().isUnprocessableEntity());

        verify(votoService).criarVoto(refEq(votoReq), eq(5L));

    }
}
