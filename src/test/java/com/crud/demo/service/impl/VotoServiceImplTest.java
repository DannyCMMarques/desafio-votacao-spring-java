package com.crud.demo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.domain.Associado;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.Voto;
import com.crud.demo.domain.enums.VotoEnum;
import com.crud.demo.repositories.VotoRepository;
import com.crud.demo.service.ContagemService;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;
import com.crud.demo.service.dto.voto.VotoRequestDTO;
import com.crud.demo.service.dto.voto.VotoResponseDTO;
import com.crud.demo.service.mappers.VotoMapper;
import com.crud.demo.service.validacoes.AssociadoValidacaoService;
import com.crud.demo.service.validacoes.SessaoValidacaoService;
import com.crud.demo.service.validacoes.VotoValidacaoService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do VotoServiceImpl")
class VotoServiceImplTest {

    @Mock
    private VotoRepository votoRepository;
    @Mock
    private VotoMapper votoMapper;

    @Mock
    private SessaoValidacaoService sessaoValidacao;
    @Mock
    private AssociadoValidacaoService associadoValidacao;
    @Mock
    private VotoValidacaoService votoValidacao;
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
        when(sessaoValidacao.validarEObterSessao(1L)).thenReturn(sessao);
        when(associadoValidacao.validarExistencia(2L)).thenReturn(associado);
        when(votoMapper.toEntity(requestDTO, sessao, associado)).thenReturn(votoEntity);
        when(votoRepository.save(votoEntity)).thenReturn(votoEntity);
        when(votoMapper.toDTO(votoEntity)).thenReturn(responseDTO);

        VotoResponseDTO resultado = votoService.criarVoto(requestDTO, 1L);

        assertThat(resultado).isEqualTo(responseDTO);

        verify(sessaoValidacao).validarEObterSessao(1L);
        verify(associadoValidacao).validarExistencia(2L);
        verify(votoValidacao).validar(sessao, associado);
        verify(votoMapper).toEntity(requestDTO, sessao, associado);
        verify(votoRepository).save(votoEntity);
        verify(contagemService).executar(sessao);
        verify(votoMapper).toDTO(votoEntity);
    }
}
