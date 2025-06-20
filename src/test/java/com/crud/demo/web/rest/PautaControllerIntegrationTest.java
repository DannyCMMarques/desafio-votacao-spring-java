// package com.crud.demo.web.rest;

// import java.util.List;

// import static org.hamcrest.Matchers.endsWith;
// import static org.hamcrest.Matchers.is;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.ArgumentMatchers.refEq;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import com.crud.demo.exceptions.pauta.PautaNaoCadastradaException;
// import com.crud.demo.service.dto.pauta.PautaRequestDTO;
// import com.crud.demo.service.dto.pauta.PautaResponseDTO;
// import com.crud.demo.service.impl.PautaServiceImpl;
// import com.fasterxml.jackson.databind.ObjectMapper;

// @WebMvcTest(PautaController.class)
// @DisplayName("PautaController – testes de endpoints REST")
// class PautaControllerIntegrationTest {

//     @Autowired
//     private MockMvc mockMvc;
//     @Autowired
//     private ObjectMapper mapper;
//     @MockBean
//     private PautaServiceImpl pautaService;

//     private PautaRequestDTO pautaRequest;
//     private PautaResponseDTO pautaResponse;

//     @BeforeEach
//     void setUp() {
//         pautaRequest = new PautaRequestDTO();
//         pautaRequest.setTitulo("Nova Pauta");
//         pautaRequest.setDescricao("Descrição");

//         pautaResponse = new PautaResponseDTO();
//         pautaResponse.setId(1L);
//         pautaResponse.setTitulo("Nova Pauta");
//         pautaResponse.setDescricao("Descrição");
//     }

//     @Test
//     @DisplayName(" Deve cadastrar pauta e retornar Location absoluto")
//     void deveCadastrarComSucesso() throws Exception {
//         when(pautaService.criarPauta(refEq(pautaRequest)))
//                 .thenReturn(pautaResponse);

//         mockMvc.perform(post("/api/v1/pauta")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(mapper.writeValueAsString(pautaRequest)))
//                 .andExpect(status().isCreated())
//                 .andExpect(header().string(HttpHeaders.LOCATION,
//                         endsWith("/api/v1/pauta/1")))
//                 .andExpect(jsonPath("$.id", is(1)));

//         verify(pautaService).criarPauta(refEq(pautaRequest));
//     }

//     @Test
//     @DisplayName(" Deve listar pautas paginadas")
//     void deveListarComSucesso() throws Exception {
//         Pageable pageable = PageRequest.of(0, 10);
//         when(pautaService.listarPautas(0, 10, "titulo", "asc"))
//                 .thenReturn(new PageImpl<>(List.of(pautaResponse), pageable, 1));

//         mockMvc.perform(get("/api/v1/pauta")
//                 .param("page", "0").param("size", "10")
//                 .param("sortBy", "titulo").param("direction", "asc"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.content[0].id", is(1)));

//         verify(pautaService).listarPautas(0, 10, "titulo", "asc");

//     }

//     @Test
//     @DisplayName(" Deve retornar pauta existente")
//     void deveBuscarPorIdComSucesso() throws Exception {
//         when(pautaService.buscarPorId(1L)).thenReturn(pautaResponse);

//         mockMvc.perform(get("/api/v1/pauta/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id", is(1)));

//         verify(pautaService).buscarPorId(1L);
//     }

//     @Test
//     @DisplayName(" Deve retornar quando pauta não encontrada")
//     void deveRetornar404QuandoNaoEncontrada() throws Exception {
//         when(pautaService.buscarPorId(99L))
//                 .thenThrow(new PautaNaoCadastradaException());

//         mockMvc.perform(get("/api/v1/pauta/99"))
//                 .andExpect(status().isNotFound());

//         verify(pautaService).buscarPorId(99L);
//     }

//     @Test
//     @DisplayName(" Deve atualizar pauta existente")
//     void deveAtualizarComSucesso() throws Exception {
//         PautaRequestDTO pautaRequest = new PautaRequestDTO();
//         pautaRequest.setTitulo("Atualizada");
//         pautaRequest.setDescricao("Descrição Editada");

//         PautaResponseDTO atualizado = new PautaResponseDTO();
//         atualizado.setId(1L);
//         atualizado.setTitulo("Atualizada");
//         atualizado.setDescricao("Descrição Editada");

//         when(pautaService.atualizarPauta(eq(1L), refEq(pautaRequest)))
//                 .thenReturn(atualizado);

//         mockMvc.perform(put("/api/v1/pauta/1")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(mapper.writeValueAsString(pautaRequest)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.titulo", is("Atualizada")));

//         verify(pautaService).atualizarPauta(eq(1L), refEq(pautaRequest));
//     }

//     @Test
//     @DisplayName(" Deve deletar pauta existente")
//     void deveDeletarComSucesso() throws Exception {
//         mockMvc.perform(delete("/api/v1/pauta/1"))
//                 .andExpect(status().isNoContent());

//         verify(pautaService).deletarPauta(1L);
//     }

// }
