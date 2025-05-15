package com.crud.demo.web.rest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.crud.demo.exceptions.associado.AssociadoNaoEncontradoException;
import com.crud.demo.exceptions.handler.GlobalExceptionHandler;
import com.crud.demo.service.AssociadoService;
import com.crud.demo.service.AssociadoValidacaoService;
import com.crud.demo.service.dto.associado.AssociadoRequestDTO;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AssociadoController.class)
@Import(GlobalExceptionHandler.class)
public class AssociadoControllerIntegrationTest {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AssociadoService associadoService;

        @MockBean
        private AssociadoValidacaoService associadoValidacaoService;
        @Autowired
        private ObjectMapper objectMapper;

        private AssociadoRequestDTO associadoRequest;
        private AssociadoResponseDTO associadoResponse;

        @BeforeEach
        void setUp() {
                associadoRequest = new AssociadoRequestDTO();
                associadoRequest.setNome("João da Silva");
                associadoRequest.setCpf("12344448901");

                associadoResponse = AssociadoResponseDTO.builder()
                                .id(1L)
                                .nome("João da Silva")
                                .build();

        }

        @DisplayName("Deve cadastrar associado e retornar 201 Created")
        @Test
        void deveCadastrarAssociadoComSucesso() throws Exception {

                Mockito.when(associadoService.cadastrarAssociado(associadoRequest)).thenReturn(associadoResponse);

                mockMvc.perform(post("/api/v1/associado")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(associadoRequest)))
                                .andExpect(status().isCreated())
                                .andExpect(header().string("Location", "http://localhost/api/v1/associado/1"))
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.nome").value("João da Silva"));

                verify(associadoService).cadastrarAssociado(any(AssociadoRequestDTO.class));

        }

        @Test
        @DisplayName("Deve retornar 400 Bad Request ao cadastrar associado com dados inválidos")

        void deveRetornarErroCadastrarAssociadoComDadosInvalidos() throws Exception {
                AssociadoRequestDTO invalido = new AssociadoRequestDTO();
                invalido.setCpf("123");

                mockMvc.perform(post("/api/v1/associado")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalido)))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(associadoService);
        }

        @Test
        @DisplayName("Deve buscar associado por ID e retornar 200 OK")
        void deveBuscarAssociadoPorIdSucesso() throws Exception {

                Mockito.when(associadoService.buscarAssociadoPorId(1L)).thenReturn(associadoResponse);

                mockMvc.perform(get("/api/v1/associado/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.nome").value("João da Silva"));

                verify(associadoService).buscarAssociadoPorId(1L);

        }

        @Test
        @DisplayName("Deve retornar 404 Not Found ao tentar buscar associado inexistente")
        void deveRetornar404AoBuscarAssociadoInexistente() throws Exception {
                Mockito.when(associadoService.buscarAssociadoPorId(99L))
                                .thenThrow(new AssociadoNaoEncontradoException());

                mockMvc.perform(get("/api/v1/associado/99"))
                                .andExpect(status().isNotFound());

                verify(associadoService).buscarAssociadoPorId(99L);
        }

        @Test
        @DisplayName("Deve listar associados com paginação e retornar 200 OK")
        void deveListarAssociadosComPaginacaoSucesso() throws Exception {
                var page = new PageImpl<>(List.of(associadoResponse), PageRequest.of(0, 10), 1);

                Mockito.when(associadoService.listarTodosAssociados(
                                eq(0),
                                eq(10),
                                eq("nome"),
                                eq("asc")))
                                .thenReturn(page);

                mockMvc.perform(get("/api/v1/associado?page=0&size=10&sortBy=nome&direction=asc"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].nome").value("João da Silva"));
                verify(associadoService).listarTodosAssociados(0, 10, "nome", "asc");

        }

        @Test
        @DisplayName("Deve atualizar associado e retornar 200 OK")
        void deveAtualizarAssociadoERetornar200() throws Exception {
                associadoRequest.setNome("Novo Nome");
                associadoRequest.setCpf("12345678901");
                associadoResponse = AssociadoResponseDTO.builder()
                                .id(1L)
                                .nome("Novo Nome")
                                .build();

                Mockito.when(associadoService.atualizarAssociado(1L, associadoRequest)).thenReturn(associadoResponse);

                mockMvc.perform(put("/api/v1/associado/1", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(associadoRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.nome").value("Novo Nome"));
                verify(associadoService).atualizarAssociado(eq(1L), any(AssociadoRequestDTO.class));

        }

        @Test
        @DisplayName("Deve deletar associado e retornar 204 No Content")

        void deveDeletarAssociadoComSucesso() throws Exception {
                mockMvc.perform(delete("/api/v1/associado/1"))
                                .andExpect(status().isNoContent());
                verify(associadoService).deletarAssociado(1L);

        }

        @DisplayName("Deve retornar 404 ao tentar deletar associado inexistente")
        @Test
        void deveFalharAoDeletarAssociadoInexistente() throws Exception {
                Long idInexistente = 99L;
                doThrow(new AssociadoNaoEncontradoException()).when(associadoService).deletarAssociado(idInexistente);

                mockMvc.perform(delete("/api/v1/associados/{id}", idInexistente)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

        }
}
