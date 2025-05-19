package com.crud.demo.service.impl.validacao;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.crud.demo.domain.Associado;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.Voto;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.exceptions.VotoDuplicadoException;
import com.crud.demo.exceptions.sessao.SessaoJaFinalizadaException;
import com.crud.demo.exceptions.sessao.SessaoNaoIniciadaException;

@DisplayName("Testes unitários do VotoValidacaoServiceImpl")
class VotoValidacaoServiceImplTest {

    private final VotoValidacaoServiceImpl service = new VotoValidacaoServiceImpl();

    private Associado associado(Long id) {
        Associado a = new Associado();
        a.setId(id);
        return a;
    }

    private Sessao novaSessao(StatusSessaoEnum status, List<Voto> votos) {
        Sessao s = new Sessao();
        s.setStatus(status);
        s.setVotos(votos);
        return s;
    }

    @Test
    @DisplayName("Deve lançar exceção quando sessão ainda não iniciada")
    void deveLancarQuandoNaoIniciada() {
        Sessao sessao = novaSessao(StatusSessaoEnum.NAO_INICIADA, List.of());
        Associado associado = associado(1L);

        assertThrows(SessaoNaoIniciadaException.class,
                () -> service.validar(sessao, associado));
    }

    @Test
    @DisplayName("Deve lançar exceção quando sessão finalizada")
    void deveLancarQuandoFinalizada() {
        Sessao sessao = novaSessao(StatusSessaoEnum.FINALIZADA, List.of());
        Associado associado = associado(1L);

        assertThrows(SessaoJaFinalizadaException.class,
                () -> service.validar(sessao, associado));
    }

    @Test
    @DisplayName("Deve lançar exceção quando associado já votou")
    void deveLancarQuandoJaVotou() {
        Associado associado = associado(1L);

        Voto voto = new Voto();
        voto.setAssociado(associado);

        Sessao sessao = novaSessao(StatusSessaoEnum.EM_ANDAMENTO,
                List.of(voto));

        assertThrows(VotoDuplicadoException.class,
                () -> service.validar(sessao, associado));
    }

    @Test
    @DisplayName("Não deve lançar exceção quando associado ainda não votou")
    void naoDeveLancarQuandoNaoVotou() {
        Associado associadoQueVaiVotar = associado(1L);
        Associado outroAssociado = associado(2L);

        Voto votoDoOutro = new Voto();
        votoDoOutro.setAssociado(outroAssociado);

        Sessao sessao = novaSessao(StatusSessaoEnum.EM_ANDAMENTO,
                List.of(votoDoOutro));

        assertDoesNotThrow(() -> service.validar(sessao, associadoQueVaiVotar));
    }

}
