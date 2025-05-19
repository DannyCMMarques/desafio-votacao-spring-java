package com.crud.demo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.crud.demo.domain.Pauta;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.service.dto.pauta.PautaRequestDTO;
import com.crud.demo.service.dto.pauta.PautaResponseDTO;
import com.crud.demo.service.mappers.PautaMapper;
import com.crud.demo.service.validacoes.PautaValidacaoService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do PautaServiceImpl")
class PautaServiceImplTest {

    @Mock 
    private PautaRepository pautaRepository;
    @Mock
     private PautaMapper pautaMapper;
    @Mock
     private PautaValidacaoService pautaValidacaoService;

    @InjectMocks private PautaServiceImpl pautaService;

    private PautaRequestDTO pautaRequestDTO;
    private Pauta pautaEntity;
    private PautaResponseDTO pautaResponseDTO;

    @BeforeEach
    void setUp() {
        pautaRequestDTO = new PautaRequestDTO();
        pautaRequestDTO.setTitulo("Título");
        pautaRequestDTO.setDescricao("Descrição");

        pautaEntity = new Pauta();
        pautaEntity.setId(1L);
        pautaEntity.setTitulo("Título");
        pautaEntity.setDescricao("Descrição");

        pautaResponseDTO = new PautaResponseDTO();
        pautaResponseDTO.setId(1L);
        pautaResponseDTO.setTitulo("Título");
        pautaResponseDTO.setDescricao("Descrição");
    }

    @Test
    @DisplayName("Deve criar pauta com sucesso")
    void deveCriarPautaComSucesso() {
        when(pautaMapper.toEntity(pautaRequestDTO)).thenReturn(pautaEntity);
        when(pautaRepository.save(pautaEntity)).thenReturn(pautaEntity);
        when(pautaMapper.toDto(pautaEntity)).thenReturn(pautaResponseDTO);

        PautaResponseDTO resultado = pautaService.criarPauta(pautaRequestDTO);

        assertThat(resultado).isEqualTo(pautaResponseDTO);
        verify(pautaMapper).toEntity(pautaRequestDTO);
        verify(pautaRepository).save(pautaEntity);
        verify(pautaMapper).toDto(pautaEntity);
        verifyNoInteractions(pautaValidacaoService);
    }

    @Test
    @DisplayName("Deve atualizar pauta com sucesso")
    void deveAtualizarPautaComSucesso() {
        Long id = 1L;
        Pauta pautaExistente = new Pauta();
        pautaExistente.setId(id);

        when(pautaValidacaoService.verificarStatusNaoVotada(id)).thenReturn(pautaExistente);
        when(pautaRepository.save(pautaExistente)).thenReturn(pautaExistente);
        when(pautaMapper.toDto(pautaExistente)).thenReturn(pautaResponseDTO);

        PautaResponseDTO resultado = pautaService.atualizarPauta(id, pautaRequestDTO);

        assertThat(resultado).isEqualTo(pautaResponseDTO);
        assertThat(pautaExistente.getTitulo()).isEqualTo(pautaRequestDTO.getTitulo());
        assertThat(pautaExistente.getDescricao()).isEqualTo(pautaRequestDTO.getDescricao());
        verify(pautaRepository).save(pautaExistente);
    }

    @Test
    @DisplayName("Deve deletar pauta com sucesso")
    void deveDeletarPautaComSucesso() {
        Long id = 1L;
        when(pautaValidacaoService.verificarStatusNaoVotada(id)).thenReturn(pautaEntity);

        pautaService.deletarPauta(id);

        verify(pautaValidacaoService).verificarStatusNaoVotada(id);
        verify(pautaRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve listar pautas com sucesso")
    void deveListarPautasComSucesso() {
        int page = 0, size = 2;
        String sortBy = "titulo", direction = "asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        Page<Pauta> pageEntity = new PageImpl<>(List.of(pautaEntity), pageable, 1);
        when(pautaRepository.findAll(pageable)).thenReturn(pageEntity);
        when(pautaMapper.toDto(pautaEntity)).thenReturn(pautaResponseDTO);

        Page<PautaResponseDTO> resultado =
                pautaService.listarPautas(page, size, sortBy, direction);

        assertThat(resultado.getContent()).containsExactly(pautaResponseDTO);
        assertThat(resultado.getTotalElements()).isEqualTo(1);
        verify(pautaRepository).findAll(pageable);
        verify(pautaMapper).toDto(pautaEntity);
    }

    @Test
    @DisplayName("Deve buscar pauta por ID com sucesso")
    void deveBuscarPautaPorIdComSucesso() {
        Long id = 1L;
        when(pautaValidacaoService.verificarStatusNaoVotada(id)).thenReturn(pautaEntity);
        when(pautaMapper.toDto(pautaEntity)).thenReturn(pautaResponseDTO);

        PautaResponseDTO resultado = pautaService.buscarPorId(id);

        assertThat(resultado).isEqualTo(pautaResponseDTO);
        verify(pautaValidacaoService).verificarStatusNaoVotada(id);
        verify(pautaMapper).toDto(pautaEntity);
    }
}
