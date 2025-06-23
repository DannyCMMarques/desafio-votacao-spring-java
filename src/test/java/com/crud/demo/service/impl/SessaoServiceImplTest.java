package com.crud.demo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.DuracaoSessaoEnum;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.repositories.SessaoRepository;
import com.crud.demo.service.dto.sessao.SessaoRequestDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;
import com.crud.demo.service.mappers.SessaoMapper;
import com.crud.demo.service.utils.DuracaoSessaoUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do SessaoServiceImpl")
class SessaoServiceImplTest {

    @Mock
    private SessaoRepository sessaoRepository;
    @Mock
    private SessaoMapper sessaoMapper;
    @Mock
    private PautaServiceImpl pautaService;

    @InjectMocks
    private SessaoServiceImpl sessaoService;

    private SessaoRequestDTO requestDTO;
    private Sessao sessaoEntidade;
    private SessaoResponseDTO responseDTO;
    private Pauta pautaEntidade;

    @BeforeEach
    void setUp() {
        requestDTO = new SessaoRequestDTO();
        requestDTO.setIdPauta(1L);
        requestDTO.setDuracao(10.0);
        requestDTO.setUnidade(DuracaoSessaoEnum.MIN);

        pautaEntidade = new Pauta();
        pautaEntidade.setId(1L);

        sessaoEntidade = new Sessao();
        sessaoEntidade.setId(1L);
        sessaoEntidade.setStatus(StatusSessaoEnum.NAO_INICIADA);

        responseDTO = SessaoResponseDTO.builder()
                .id(1L)
                .status(StatusSessaoEnum.NAO_INICIADA)
                .duracao(10.0)
                .build();
    }

    @Test
    @DisplayName("Deve criar sessão com sucesso")
    void deveCriarSessaoComSucesso() {
        double minutosConvertidos = 10.0;

        try (MockedStatic<DuracaoSessaoUtils> mockedStatic = mockStatic(DuracaoSessaoUtils.class)) {
            mockedStatic.when(() -> DuracaoSessaoUtils.converterMinutos(10.0, DuracaoSessaoEnum.MIN))
                    .thenReturn(minutosConvertidos);

            when(pautaService.buscarPautaNaoVotadaPorId(1L)).thenReturn(pautaEntidade);
            when(sessaoMapper.toEntity(requestDTO, pautaEntidade)).thenReturn(sessaoEntidade);
            when(sessaoRepository.save(sessaoEntidade)).thenReturn(sessaoEntidade);
            when(sessaoMapper.toResponseDTO(sessaoEntidade)).thenReturn(responseDTO);

            SessaoResponseDTO resultado = sessaoService.criarSessao(requestDTO);

            assertThat(resultado).isEqualTo(responseDTO);
            verify(sessaoRepository).save(sessaoEntidade);
            assertThat(sessaoEntidade.getDuracao()).isEqualTo(minutosConvertidos);
            assertThat(sessaoEntidade.getStatus()).isEqualTo(StatusSessaoEnum.NAO_INICIADA);
        }
    }

    @Test
    @DisplayName("Deve atualizar sessão com sucesso")
    void deveAtualizarSessaoComSucesso() {
        double minutosConvertidos = 30.0;
        requestDTO.setDuracao(0.5);
        requestDTO.setUnidade(DuracaoSessaoEnum.H);

        try (MockedStatic<DuracaoSessaoUtils> mockedStatic = mockStatic(DuracaoSessaoUtils.class)) {
            mockedStatic.when(() -> DuracaoSessaoUtils.converterMinutos(0.5, DuracaoSessaoEnum.H))
                    .thenReturn(minutosConvertidos);

            when(sessaoRepository.findById(1L)).thenReturn(Optional.of(sessaoEntidade));
            when(pautaService.buscarPautaNaoVotadaPorId(1L)).thenReturn(pautaEntidade);
            when(sessaoRepository.save(sessaoEntidade)).thenReturn(sessaoEntidade);
            when(sessaoMapper.toResponseDTO(sessaoEntidade)).thenReturn(responseDTO);

            SessaoResponseDTO resultado = sessaoService.atualizarSessao(1L, requestDTO);

            assertThat(resultado).isEqualTo(responseDTO);
            assertThat(sessaoEntidade.getPauta()).isEqualTo(pautaEntidade);
            assertThat(sessaoEntidade.getDuracao()).isEqualTo(minutosConvertidos);
        }
    }

    @Test
    @DisplayName("Deve listar sessões com sucesso com filtros")
    void deveListarSessoesComSucesso() {
        int page = 0, size = 2;
        String sortBy = "id", direction = "asc";
        Long pautaId = 1L;
        StatusSessaoEnum status = StatusSessaoEnum.EM_ANDAMENTO;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        Page<Sessao> pageEntity = new PageImpl<>(List.of(sessaoEntidade), pageable, 1);

        when(sessaoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pageEntity);

        when(sessaoMapper.toDto(sessaoEntidade)).thenReturn(responseDTO);

        Page<SessaoResponseDTO> resultado = sessaoService.listarSessoesComFiltro(
                page + 1, size, sortBy, direction, pautaId, status);

        assertThat(resultado.getContent()).containsExactly(responseDTO);
        assertThat(resultado.getTotalElements()).isEqualTo(1);

        verify(sessaoRepository).findAll(any(Specification.class), eq(pageable));
        verify(sessaoMapper).toDto(sessaoEntidade);
    }

    @Test
    @DisplayName("Deve buscar sessão por ID com sucesso")
    void deveBuscarSessaoPorIdComSucesso() {
        when(sessaoRepository.findById(1L)).thenReturn(Optional.of(sessaoEntidade));
        when(sessaoMapper.toDto(sessaoEntidade)).thenReturn(responseDTO);

        SessaoResponseDTO resultado = sessaoService.buscarPorId(1L);

        assertThat(resultado).isEqualTo(responseDTO);
        verify(sessaoRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve deletar sessão com sucesso")
    void deveDeletarSessaoComSucesso() {
        when(sessaoRepository.findById(1L)).thenReturn(Optional.of(sessaoEntidade));

        sessaoService.deletarSessao(1L);

        verify(sessaoRepository).delete(sessaoEntidade);
    }
}
