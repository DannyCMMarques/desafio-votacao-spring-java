package com.crud.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.crud.demo.domain.Associado;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.Voto;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.domain.enums.VotoEnum;
import com.crud.demo.repositories.AssociadoRepository;
import com.crud.demo.repositories.VotoRepository;
import com.crud.demo.service.ContagemService;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;
import com.crud.demo.service.dto.voto.VotoRequestDTO;
import com.crud.demo.service.dto.voto.VotoResponseDTO;
import com.crud.demo.service.mappers.VotoMapper;
import com.crud.demo.exceptions.VotoDuplicadoException;
import com.crud.demo.exceptions.associado.AssociadoNaoEncontradoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do VotoServiceImpl")
class VotoServiceImplTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private VotoMapper votoMapper;

    @Mock
    private SessaoServiceImpl sessaoService;

    @Mock
    private ContagemService contagemService;

    @InjectMocks
    private VotoServiceImpl votoService;

    private Sessao sessao;
    private Associado associado;
    private VotoRequestDTO requestDTO;
    private Voto votoEntity;
    private VotoResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        sessao = new Sessao();
        sessao.setId(1L);
        sessao.setStatus(StatusSessaoEnum.EM_ANDAMENTO);
        sessao.setVotos(new ArrayList<>()); 

        associado = new Associado();
        associado.setId(2L);

        requestDTO = new VotoRequestDTO(true, 2L);

        votoEntity = new Voto();
        votoEntity.setId(3L);
        votoEntity.setVoto(true);
        votoEntity.setSessao(sessao);
        votoEntity.setAssociado(associado);

        VotoEnum votoEnum = VotoEnum.SIM;
        AssociadoResponseDTO associadoResponseDTO = mock(AssociadoResponseDTO.class);
        responseDTO = VotoResponseDTO.builder()
                .id(3L)
                .voto(votoEnum)
                .associado(associadoResponseDTO)
                .build();
    }

    @Test
    @DisplayName("Deve registrar voto com sucesso")
    void deveCriarVotoComSucesso() {
        when(sessaoService.verificarSessaoAberta(1L)).thenReturn(sessao);
        when(associadoRepository.findById(2L)).thenReturn(Optional.of(associado));
        when(votoMapper.toEntity(requestDTO, sessao, associado)).thenReturn(votoEntity);
        when(votoRepository.save(votoEntity)).thenReturn(votoEntity);
        when(votoMapper.toDTO(votoEntity)).thenReturn(responseDTO);

        VotoResponseDTO resultado = votoService.criarVoto(requestDTO, 1L);

        assertThat(resultado).isEqualTo(responseDTO);

        verify(sessaoService).verificarSessaoAberta(1L);
        verify(associadoRepository).findById(2L);
        verify(votoMapper).toEntity(requestDTO, sessao, associado);
        verify(votoRepository).save(votoEntity);
        verify(contagemService).executar(sessao);
        verify(votoMapper).toDTO(votoEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção se associado já votou na sessão")
    void deveLancarExcecaoSeAssociadoJaVotou() {
        Voto votoExistente = new Voto();
        votoExistente.setAssociado(associado);
        sessao.setVotos(List.of(votoExistente));

        when(sessaoService.verificarSessaoAberta(1L)).thenReturn(sessao);
        when(associadoRepository.findById(2L)).thenReturn(Optional.of(associado));

        assertThrows(VotoDuplicadoException.class,
                () -> votoService.criarVoto(requestDTO, 1L));

        verify(sessaoService).verificarSessaoAberta(1L);
        verify(associadoRepository).findById(2L);
        verifyNoMoreInteractions(votoRepository, votoMapper, contagemService);
    }

    @Test
    @DisplayName("Deve lançar exceção se associado não for encontrado")
    void deveLancarExcecaoSeAssociadoNaoEncontrado() {
        when(sessaoService.verificarSessaoAberta(1L)).thenReturn(sessao);
        when(associadoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(AssociadoNaoEncontradoException.class,
                () -> votoService.criarVoto(requestDTO, 1L));

        verify(sessaoService).verificarSessaoAberta(1L);
        verify(associadoRepository).findById(2L);
        verifyNoMoreInteractions(votoRepository, votoMapper, contagemService);
    }
}
