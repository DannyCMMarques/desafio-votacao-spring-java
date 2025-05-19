package com.crud.demo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.repositories.SessaoRepository;
import com.crud.demo.service.SessaoService;
import com.crud.demo.service.VotoService;
import com.crud.demo.service.dto.sessao.SessaoIniciadaResponseDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;
import com.crud.demo.service.mappers.PautaMapper;
import com.crud.demo.service.mappers.SessaoMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do IniciarSessaoServiceImpl")
class IniciarSessaoServiceImplTest {

    @Mock
    private VotoService votoService;
    @Mock
    private SessaoService sessaoService;
    @Mock
    private PautaMapper pautaMapper;
    @Mock 
    private SessaoMapper sessaoMapper;
    @Mock
    private PautaRepository pautaRepository;
    @Mock
    private SessaoRepository sessaoRepository;

    @InjectMocks
    private IniciarSessaoServiceImpl iniciarSessaoService;

    private Pauta pauta;
    private Sessao sessaoEntity;
    private SessaoResponseDTO sessaoResponseDTO;
    private SessaoIniciadaResponseDTO sessaoIniciadaDTO;

    @BeforeEach
    void setUp() {
        pauta = spy(new Pauta());
        pauta.setId(10L);

        sessaoEntity = spy(new Sessao());
        sessaoEntity.setId(1L);
        sessaoEntity.setStatus(StatusSessaoEnum.NAO_INICIADA);
        sessaoEntity.setPauta(pauta);

        sessaoResponseDTO = SessaoResponseDTO.builder()
                .id(1L)
                .status(StatusSessaoEnum.NAO_INICIADA)
                .build();

        sessaoIniciadaDTO = SessaoIniciadaResponseDTO.builder()
                .id(1L)
                .status(StatusSessaoEnum.EM_ANDAMENTO)
                .build();
    }

    @Test
    @DisplayName("Deve iniciar sessão e pauta com sucesso")
    void deveExecutarInicioSessaoComSucesso() {
        when(sessaoService.buscarPorId(1L)).thenReturn(sessaoResponseDTO);
        when(sessaoMapper.toEntity(sessaoResponseDTO)).thenReturn(sessaoEntity);
        when(sessaoRepository.save(sessaoEntity)).thenReturn(sessaoEntity);
        when(sessaoMapper.toIniciadaResponseDTO(sessaoEntity)).thenReturn(sessaoIniciadaDTO);
        when(pautaRepository.save(pauta)).thenReturn(pauta);

        SessaoIniciadaResponseDTO resultado = iniciarSessaoService.executar(1L);

        assertThat(resultado).isEqualTo(sessaoIniciadaDTO);
        verify(sessaoService).buscarPorId(1L);
        verify(sessaoMapper).toEntity(sessaoResponseDTO);
        verify(sessaoEntity).iniciarSessao(any(LocalDateTime.class));
        verify(pauta).iniciarVotacaoPauta();
        verify(sessaoRepository).save(sessaoEntity);
        verify(pautaRepository).save(pauta);
    }

    @Test
    @DisplayName("Deve iniciar somente a sessão quando chamado iniciarSessao")
    void deveIniciarSessaoIndividualmente() {
        when(sessaoService.buscarPorId(1L)).thenReturn(sessaoResponseDTO);
        when(sessaoMapper.toEntity(sessaoResponseDTO)).thenReturn(sessaoEntity);
        when(sessaoRepository.save(sessaoEntity)).thenReturn(sessaoEntity);

        Sessao retorno = iniciarSessaoService.iniciarSessao(1L);

        assertThat(retorno).isEqualTo(sessaoEntity);
        verify(sessaoEntity).iniciarSessao(any(LocalDateTime.class));
        verify(sessaoRepository).save(sessaoEntity);
    }

    @Test
    @DisplayName("Deve iniciar somente a pauta quando chamado iniciarPauta")
    void deveIniciarPautaIndividualmente() {
        when(pautaRepository.save(pauta)).thenReturn(pauta);

        iniciarSessaoService.iniciarPauta(sessaoEntity);

        verify(pauta).iniciarVotacaoPauta();
        verify(pautaRepository).save(pauta);
    }
}