package com.crud.demo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

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
import org.springframework.data.jpa.domain.Specification;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.enums.StatusPautaEnum;
import com.crud.demo.exceptions.pauta.PautaNaoCadastradaException;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.service.dto.pauta.PautaRequestDTO;
import com.crud.demo.service.dto.pauta.PautaResponseDTO;
import com.crud.demo.service.mappers.PautaMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do PautaServiceImpl")
class PautaServiceImplTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private PautaMapper pautaMapper;

    @InjectMocks
    private PautaServiceImpl pautaService;

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
    }

    @Test
    @DisplayName("Deve atualizar pauta com sucesso")
    void deveAtualizarPautaComSucesso() {
        Long id = 1L;
        when(pautaRepository.findById(id)).thenReturn(Optional.of(pautaEntity));
        when(pautaRepository.save(pautaEntity)).thenReturn(pautaEntity);
        when(pautaMapper.toDto(pautaEntity)).thenReturn(pautaResponseDTO);

        PautaResponseDTO resultado = pautaService.atualizarPauta(id, pautaRequestDTO);

        assertThat(resultado).isEqualTo(pautaResponseDTO);
        assertThat(pautaEntity.getTitulo()).isEqualTo(pautaRequestDTO.getTitulo());
        assertThat(pautaEntity.getDescricao()).isEqualTo(pautaRequestDTO.getDescricao());

        verify(pautaRepository).findById(id);
        verify(pautaRepository).save(pautaEntity);
        verify(pautaMapper).toDto(pautaEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar pauta inexistente")
    void deveLancarExcecaoAoAtualizarPautaInexistente() {
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PautaNaoCadastradaException.class,
            () -> pautaService.atualizarPauta(99L, pautaRequestDTO));
    }

    @Test
    @DisplayName("Deve deletar pauta com sucesso")
    void deveDeletarPautaComSucesso() {
        Long id = 1L;
        when(pautaRepository.findById(id)).thenReturn(Optional.of(pautaEntity));

        pautaService.deletarPauta(id);

        verify(pautaRepository).findById(id);
        verify(pautaRepository).delete(pautaEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar pauta inexistente")
    void deveLancarExcecaoAoDeletarPautaInexistente() {
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PautaNaoCadastradaException.class, () -> pautaService.deletarPauta(99L));
    }

  @Test
@DisplayName("Deve listar pautas com sucesso com filtros de título e status")
void deveListarPautasComSucesso() {
    int page = 1, size = 2;
    String sortBy = "titulo", direction = "asc";
    String titulo = "Reforma", 
           directionLower = direction.toLowerCase();
    StatusPautaEnum status = StatusPautaEnum.NAO_VOTADA;

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortBy).ascending());

    Page<Pauta> pageEntity = new PageImpl<>(List.of(pautaEntity), pageable, 1);

    when(pautaRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(pageEntity);
    when(pautaMapper.toDto(pautaEntity)).thenReturn(pautaResponseDTO);

    Page<PautaResponseDTO> resultado = pautaService.listarPautas(page, size, sortBy, direction, titulo, status);

    assertThat(resultado.getContent()).containsExactly(pautaResponseDTO);
    assertThat(resultado.getTotalElements()).isEqualTo(1);

    verify(pautaRepository).findAll(any(Specification.class), eq(pageable));
    verify(pautaMapper).toDto(pautaEntity);
}

    @Test
    @DisplayName("Deve buscar pauta por ID com sucesso")
    void deveBuscarPautaPorIdComSucesso() {
        Long id = 1L;
        when(pautaRepository.findById(id)).thenReturn(Optional.of(pautaEntity));
        when(pautaMapper.toDto(pautaEntity)).thenReturn(pautaResponseDTO);

        PautaResponseDTO resultado = pautaService.buscarPorId(id);

        assertThat(resultado).isEqualTo(pautaResponseDTO);
        verify(pautaRepository).findById(id);
        verify(pautaMapper).toDto(pautaEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar pauta inexistente")
    void deveLancarExcecaoAoBuscarPautaInexistente() {
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PautaNaoCadastradaException.class, () -> pautaService.buscarPorId(99L));
    }
}
