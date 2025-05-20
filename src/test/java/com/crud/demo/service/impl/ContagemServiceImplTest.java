
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

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.Voto;
import com.crud.demo.repositories.PautaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do ContagemServiceImpl")
class ContagemServiceImplTest {

    @Mock 
    private PautaRepository pautaRepository;

    @InjectMocks
     private ContagemServiceImpl contagemService;

    private Pauta pauta;
    private Sessao sessao;
    private Voto votoSim1;
    private Voto votoSim2;
    private Voto votoNao1;

    @BeforeEach
    void setUp() {
        pauta = new Pauta();
        pauta.setId(1L);

        votoSim1 = new Voto();
        votoSim1.setVoto(true);
        votoSim2 = new Voto();
        votoSim2.setVoto(true);
        votoNao1 = new Voto();
        votoNao1.setVoto(false);

        sessao = new Sessao();
        sessao.setPauta(pauta);
        sessao.setVotos(List.of(votoSim1, votoSim2, votoNao1));
    }

    @Test
    @DisplayName("Deve contabilizar votos e salvar pauta com sucesso")
    void deveExecutarContagemComSucesso() {
        contagemService.executar(sessao);

        assertThat(pauta.getVotosFavor()).isEqualTo(2L);
        assertThat(pauta.getVotosContra()).isEqualTo(1L);
        verify(pautaRepository).save(pauta);
    }

    @Test
    @DisplayName("Deve retornar quantidade correta de votos favoráveis")
    void deveRetornarQuantidadeVotosFavoraveis() {
        Long resultado = contagemService.votosAfavor(sessao);

        assertThat(resultado).isEqualTo(2L);
    }
  @Test
    @DisplayName("Deve retornar quantidade correta de votos contrários")
    void deveRetornarQuantidadeVotosContrarios() {
        Long resultado = contagemService.votosContra(sessao);

        assertThat(resultado).isEqualTo(1L);
    }
}
