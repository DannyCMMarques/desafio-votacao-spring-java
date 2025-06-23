package com.crud.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.crud.demo.domain.Associado;
import com.crud.demo.repositories.AssociadoRepository;
import com.crud.demo.service.dto.associado.AssociadoRequestDTO;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;
import com.crud.demo.service.mappers.AssociadoMapper;
import com.crud.demo.exceptions.associado.CPFJaCadastradoException;
import com.crud.demo.exceptions.associado.AssociadoNaoEncontradoException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do AssociadoServiceImpl")
class AssociadoServiceImplTest {

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private AssociadoMapper associadoMapper;

    @InjectMocks
    private AssociadoServiceImpl associadoService;

    @Mock
    private VotoServiceImpl votoService;

    private AssociadoRequestDTO associadoRequest;
    private Associado associadoSalvo;
    private AssociadoResponseDTO associadoResponseDTO;

    @BeforeEach
    void setUp() {
        associadoRequest = new AssociadoRequestDTO();
        associadoRequest.setNome("João da Silva");
        associadoRequest.setCpf("12344448901");

        associadoSalvo = new Associado();
        associadoSalvo.setId(1L);
        associadoSalvo.setNome("João da Silva");
        associadoSalvo.setCpf("12344448901");

        associadoResponseDTO = AssociadoResponseDTO.builder()
                .id(1L)
                .nome("João da Silva")
                .build();
    }

    @Test
    @DisplayName("Deve cadastrar associado com sucesso")
    void deveCadastrarAssociado() {
        when(associadoRepository.findByCpf(associadoRequest.getCpf())).thenReturn(Optional.empty());
        when(associadoMapper.toEntity(associadoRequest)).thenReturn(associadoSalvo);
        when(associadoRepository.save(associadoSalvo)).thenReturn(associadoSalvo);
        when(associadoMapper.toDTO(associadoSalvo)).thenReturn(associadoResponseDTO);

        AssociadoResponseDTO resultado = associadoService.cadastrarAssociado(associadoRequest);

        assertEquals("João da Silva", resultado.getNome());
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar associado com CPF já existente")
    void deveLancarExcecaoAoCadastrarComCPFJaExistente() {
        when(associadoRepository.findByCpf(associadoRequest.getCpf()))
                .thenReturn(Optional.of(new Associado()));

        assertThrows(CPFJaCadastradoException.class,
                () -> associadoService.cadastrarAssociado(associadoRequest));
    }

    @Test
    @DisplayName("Deve buscar associado por ID com sucesso")
    void deveBuscarAssociadoPorId() {
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associadoSalvo));
        when(associadoMapper.toDTO(associadoSalvo)).thenReturn(associadoResponseDTO);

        AssociadoResponseDTO resultado = associadoService.buscarAssociadoPorId(1L);

        assertEquals("João da Silva", resultado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar associado inexistente")
    void deveLancarExcecaoAoBuscarAssociadoInexistente() {
        when(associadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AssociadoNaoEncontradoException.class,
                () -> associadoService.buscarAssociadoPorId(99L));
    }

    @Test
    @DisplayName("Deve listar todos os associados")
    void deveListarTodosAssociados() {
        PageImpl<Associado> page = new PageImpl<>(List.of(associadoSalvo));

        doReturn(page)
                .when(associadoRepository)
                .findAll(any(Specification.class), any(Pageable.class));

        when(associadoMapper.toDTO(associadoSalvo)).thenReturn(associadoResponseDTO);

        Page<AssociadoResponseDTO> resultado = associadoService.listarTodosAssociados(1, 10, "id", "asc", null);

        assertEquals(1, resultado.getTotalElements());
        assertEquals("João da Silva", resultado.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar associado com CPF de outro usuário")
    void deveLancarExcecaoAoAtualizarComCPFDeOutro() {
        Associado outro = new Associado();
        outro.setId(2L);
        outro.setCpf("99999999999");

        AssociadoRequestDTO requestAtualizacao = new AssociadoRequestDTO();
        requestAtualizacao.setNome("João Atualizado");
        requestAtualizacao.setCpf("99999999999");

        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associadoSalvo));
        when(associadoRepository.findByCpf("99999999999")).thenReturn(Optional.of(outro));

        assertThrows(CPFJaCadastradoException.class,
                () -> associadoService.atualizarAssociado(1L, requestAtualizacao));
    }

    @Test
    @DisplayName("Deve deletar associado com sucesso")
    void deveDeletarAssociado() {
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associadoSalvo));

        assertDoesNotThrow(() -> associadoService.deletarAssociado(1L));
        verify(associadoRepository, times(1)).delete(associadoSalvo);
    }
}
