package com.crud.demo.service.impl.validacao;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.DuracaoSessaoEnum;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.exceptions.sessao.DuracaoMinimaException;
import com.crud.demo.exceptions.sessao.SessaoJaIniciadaException;
import com.crud.demo.exceptions.sessao.SessaoNaoCadastradaException;
import com.crud.demo.repositories.SessaoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do SessaoValidacaoServiceImpl")
class SessaoValidacaoServiceImplTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @InjectMocks
    private SessaoValidacaoServiceImpl service;

    @Test
    @DisplayName("Deve retornar sessão quando existir")
    void deveRetornarSessaoQuandoExistir() {
        Sessao sessao = new Sessao();
        sessao.setId(1L);

        when(sessaoRepository.findById(1L)).thenReturn(Optional.of(sessao));

        Sessao resultado = service.validarEObterSessao(1L);

        assertThat(resultado).isEqualTo(sessao);
    }

    @Test
    @DisplayName("Deve lançar exceção quando sessão não existir")
    void deveLancarQuandoNaoExistir() {
        when(sessaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SessaoNaoCadastradaException.class,
                () -> service.validarEObterSessao(1L));

    }

    @Test
    @DisplayName("Deve retornar sessão quando ainda não iniciada")
    void deveRetornarQuandoNaoIniciada() {
        Sessao sessao = new Sessao();
        sessao.setId(2L);
        sessao.setStatus(StatusSessaoEnum.NAO_INICIADA);

        when(sessaoRepository.findById(2L)).thenReturn(Optional.of(sessao));

        Sessao resultado = service.validarAcao(2L);

        assertThat(resultado).isEqualTo(sessao);
    }

    @Test
    @DisplayName("Deve lançar exceção quando sessão já iniciada")
    void deveLancarQuandoJaIniciada() {
        Sessao sessao = new Sessao();
        sessao.setId(2L);
        sessao.setStatus(StatusSessaoEnum.EM_ANDAMENTO);

        when(sessaoRepository.findById(2L)).thenReturn(Optional.of(sessao));

        assertThrows(SessaoJaIniciadaException.class,
                () -> service.validarAcao(2L));

    }

    @Test
    @DisplayName("Deve lançar exceção para duração < 30 SEG")
    void deveLancarParaMenosDe30Seg() {
        assertThrows(DuracaoMinimaException.class,
                () -> service.verificarDuracao(25.0, DuracaoSessaoEnum.SEG));
    }

    @Test
    @DisplayName("Deve lançar exceção para duração < 0.5 MIN")
    void deveLancarParaMenosDeMeioMinuto() {
        assertThrows(DuracaoMinimaException.class,
                () -> service.verificarDuracao(0.3, DuracaoSessaoEnum.MIN));
    }

    @Test
    @DisplayName("Não deve lançar exceção para duração válida")
    void naoDeveLancarParaDuracaoValida() {
        service.verificarDuracao(45.0, DuracaoSessaoEnum.SEG); 
        service.verificarDuracao(1.0, DuracaoSessaoEnum.MIN); 
    }

    @Nested
    @DisplayName("Método validarEObterSessao(id, duração, unidade)")
    class ValidarEObterComDuracao {

        @Test
        @DisplayName("Deve validar duração e retornar sessão quando existir")
        void deveRetornarSessaoQuandoDuracaoValida() {
            Sessao sessao = new Sessao();
            sessao.setId(3L);

            when(sessaoRepository.findById(3L)).thenReturn(Optional.of(sessao));

            Sessao resultado = service.validarEObterSessao(3L, 60.0, DuracaoSessaoEnum.SEG);

            assertThat(resultado).isEqualTo(sessao);
        }

        @Test
        @DisplayName("Deve lançar exceção de duração mínima antes de buscar sessão")
        void deveLancarDuracaoMinimaAntesDeBuscar() {
            assertThrows(DuracaoMinimaException.class,
                    () -> service.validarEObterSessao(3L, 20.0, DuracaoSessaoEnum.SEG));

            verifyNoInteractions(sessaoRepository); 
        }
    }
}
