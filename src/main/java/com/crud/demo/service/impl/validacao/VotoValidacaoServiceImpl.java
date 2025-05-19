package com.crud.demo.service.impl.validacao;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Associado;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.exceptions.VotoDuplicadoException;
import com.crud.demo.exceptions.sessao.SessaoJaFinalizadaException;
import com.crud.demo.exceptions.sessao.SessaoNaoIniciadaException;
import com.crud.demo.service.validacoes.VotoValidacaoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VotoValidacaoServiceImpl implements VotoValidacaoService {

    @Override
    public void validar(Sessao sessao, Associado associado) {
        this.verificarSessaoAberta(sessao);
        this.verificarVotoDuplicado(sessao, associado);
    }

    private void verificarSessaoAberta(Sessao sessao) {
        StatusSessaoEnum statusSessao = sessao.getStatus();
        if (statusSessao == StatusSessaoEnum.NAO_INICIADA) {
            throw new SessaoNaoIniciadaException();
        } else if (statusSessao == StatusSessaoEnum.FINALIZADA) {
            throw new SessaoJaFinalizadaException();
        }
    }

    private void verificarVotoDuplicado(Sessao sessao, Associado associado) {
        boolean jaVotou = sessao.getVotos().stream()
                .anyMatch(voto -> voto.getAssociado().equals(associado));
        if (jaVotou) {
            throw new VotoDuplicadoException();
        }
    }
}
