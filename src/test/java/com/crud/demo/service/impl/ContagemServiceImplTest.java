package com.crud.demo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.repositories.VotoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do ContagemServiceImpl")
class ContagemServiceImplTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private ContagemServiceImpl contagemService;

    private Pauta pauta;
    private Sessao sessao;

    @BeforeEach
    void setUp() {
        pauta = new Pauta();
        pauta.setId(1L);

        sessao = new Sessao();
        sessao.setId(10L);
        sessao.setPauta(pauta);
    }

    @Test
    @DisplayName("Deve contabilizar votos e salvar pauta com sucesso")
    void deveExecutarContagemComSucesso() {
        when(votoRepository.countBySessaoAndVoto(sessao, true)).thenReturn(2L);
        when(votoRepository.countBySessaoAndVoto(sessao, false)).thenReturn(1L);

        contagemService.executar(sessao);

        assertThat(pauta.getVotosFavor()).isEqualTo(2L);
        assertThat(pauta.getVotosContra()).isEqualTo(1L);
        verify(pautaRepository).save(pauta);
    }

    @Test
    @DisplayName("Deve retornar quantidade correta de votos favoráveis")
    void deveRetornarQuantidadeVotosFavoraveis() {
        when(votoRepository.countBySessaoAndVoto(sessao, true)).thenReturn(2L);

        Long resultado = contagemService.votosAfavor(sessao);

        assertThat(resultado).isEqualTo(2L);
    }

    @Test
    @DisplayName("Deve retornar quantidade correta de votos contrários")
    void deveRetornarQuantidadeVotosContrarios() {
        when(votoRepository.countBySessaoAndVoto(sessao, false)).thenReturn(1L);

        Long resultado = contagemService.votosContra(sessao);

        assertThat(resultado).isEqualTo(1L);
    }
}
