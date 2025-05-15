package com.crud.demo.service.impl;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.crud.demo.domain.Associado;
import com.crud.demo.repositories.AssociadoRepository;
import com.crud.demo.service.AssociadoValidacaoService;
import com.crud.demo.service.dto.associado.AssociadoRequestDTO;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;
import com.crud.demo.service.mappers.AssociadoMapper;

public class AssociadoServiceImplTest {

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private AssociadoValidacaoService associadoValidacaoService;

    @Mock
    private AssociadoMapper associadoMapper;

    @InjectMocks
    private AssociadoServiceImpl associadoService;

    AssociadoRequestDTO associadoRequest;
    Associado associadoEntity;
    Associado associadoSalvo;
    AssociadoResponseDTO associadoResponseDTO;
    String nomeAssociado = "João da Silva";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        associadoRequest = new AssociadoRequestDTO();
        associadoRequest.setNome("João da Silva");
        associadoRequest.setCpf("12344448901");

        associadoResponseDTO = AssociadoResponseDTO.builder()
                .id(1L)
                .nome("João da Silva")
                .build();
        associadoSalvo = new Associado();
        associadoSalvo.setId(1L);
        associadoSalvo.setNome("João da Silva");
        associadoSalvo.setCpf("12344448901");

    }

    @Test
    @DisplayName("Deve cadastrar associado com sucesso")
    void deveCadastrarAssociado() {
        doNothing().when(associadoValidacaoService).validarCPFCadastro(associadoRequest.getCpf());
        when(associadoMapper.toEntity(associadoRequest)).thenReturn(associadoEntity);
        when(associadoRepository.save(associadoEntity)).thenReturn(associadoSalvo);
        when(associadoMapper.toDTO(associadoSalvo)).thenReturn(associadoResponseDTO);

        AssociadoResponseDTO resultado = associadoService.cadastrarAssociado(associadoRequest);

        assertEquals(nomeAssociado, resultado.getNome());
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar associado com CPF inválido")
    void deveFalharAoCadastrarComCPFInvalido() {
        doThrow(new IllegalArgumentException("CPF inválido"))
                .when(associadoValidacaoService).validarCPFCadastro(associadoRequest.getCpf());

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> associadoService.cadastrarAssociado(associadoRequest));

        assertEquals("CPF inválido", ex.getMessage());
    }

    @Test
    @DisplayName("Deve buscar associado por ID com sucesso")

    void deveBuscarAssociadoPorId() {
        when(associadoValidacaoService.validarExistencia(1L)).thenReturn(associadoSalvo);
        when(associadoMapper.toDTO(associadoSalvo)).thenReturn(associadoResponseDTO);

        AssociadoResponseDTO resultado = associadoService.buscarAssociadoPorId(1L);

        assertEquals("João da Silva", resultado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar associado inexistente")
    void deveFalharAoBuscarAssociadoInexistente() {
        when(associadoValidacaoService.validarExistencia(99L))
                .thenThrow(new RuntimeException("Associado não encontrado"));

        Exception ex = assertThrows(RuntimeException.class, () -> associadoService.buscarAssociadoPorId(99L));

        assertEquals("Associado não encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Deve listar todos os associados")

    void deveListarTodosAssociados() {
        Page<Associado> page = new PageImpl<>(List.of(associadoSalvo));

        when(associadoRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(associadoMapper.toDTO(associadoSalvo)).thenReturn(associadoResponseDTO);

        Page<AssociadoResponseDTO> resultado = associadoService.listarTodosAssociados(0, 10, "id", "asc");

        assertEquals(1, resultado.getTotalElements());
        assertEquals(nomeAssociado, resultado.getContent().get(0).getNome());
    }

    @Test
    void deveAtualizarAssociado() {
        associadoSalvo.setNome("Joao Atualizado");
        Associado associadoAtualizado = associadoSalvo;

        when(associadoValidacaoService.validarExistencia(1L)).thenReturn(associadoSalvo);
        doNothing().when(associadoValidacaoService).validarCPFAtualizacao("12345678900", 1L);
        when(associadoRepository.save(associadoSalvo)).thenReturn(associadoAtualizado);
        when(associadoMapper.toDTO(associadoAtualizado)).thenReturn(associadoResponseDTO);

        AssociadoResponseDTO resultado = associadoService.atualizarAssociado(1L, associadoRequest);

        assertEquals(nomeAssociado, resultado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar com CPF inválido")
    void deveFalharAoAtualizarComCPFInvalido() {
        when(associadoValidacaoService.validarExistencia(1L)).thenReturn(associadoSalvo);
        doThrow(new IllegalArgumentException("CPF inválido"))
                .when(associadoValidacaoService).validarCPFAtualizacao("12344448901", 1L);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> associadoService.atualizarAssociado(1L, associadoRequest));

        assertEquals("CPF inválido", ex.getMessage());
    }

    @Test
        @DisplayName("Deve deletar com sucesso o associado")
    void deveDeletarAssociado() {
        when(associadoValidacaoService.validarExistencia(1L)).thenReturn(associadoSalvo);
        doNothing().when(associadoRepository).delete(associadoSalvo);

        assertDoesNotThrow(() -> associadoService.deletarAssociado(1L));
        verify(associadoRepository, times(1)).delete(associadoSalvo);
    }

}
