package com.crud.demo.web.rest;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.exceptions.sessao.SessaoNaoCadastradaException;
import com.crud.demo.service.IniciarSessaoService;
import com.crud.demo.service.dto.sessao.SessaoIniciadaResponseDTO;

@WebMvcTest(IniciarSessaoController.class)
class IniciarSessaoControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private IniciarSessaoService iniciarSessaoService;

        @Test
        @DisplayName("Deve retornar 201 Created com Location e corpo JSON corretos")
        void deveIniciarSessaoComSucesso() throws Exception {
                Long idSessao = 5L;
                SessaoIniciadaResponseDTO dto = SessaoIniciadaResponseDTO.builder()
                                .id(idSessao)
                                .status(StatusSessaoEnum.EM_ANDAMENTO)
                                .build();

                when(iniciarSessaoService.executar(idSessao)).thenReturn(dto);

                mockMvc.perform(
                                patch("/api/v1/sessao/{id}/start", idSessao)
                                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(header().string("Location", "/api/v1/votacao/" + idSessao + "/start"))
                                .andExpect(jsonPath("$.id", is(idSessao.intValue())))
                                .andExpect(jsonPath("$.status", is("EM_ANDAMENTO")));

                verify(iniciarSessaoService).executar(eq(idSessao));
        }

        @Test
        @DisplayName("Deve retornar 404 Not Found quando a sessão não existir")
        void deveRetornar404QuandoSessaoNaoEncontrada() throws Exception {
                Long idInexistente = 99L;
                when(iniciarSessaoService.executar(idInexistente))
                                .thenThrow(new SessaoNaoCadastradaException());

                mockMvc.perform(
                                patch("/api/v1/sessao/{id}/start", idInexistente)
                                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verify(iniciarSessaoService).executar(eq(idInexistente));

        }
}
