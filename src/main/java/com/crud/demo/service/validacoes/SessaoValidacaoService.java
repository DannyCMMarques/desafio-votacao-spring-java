package com.crud.demo.service.validacoes;

import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.DuracaoSessaoEnum;

public interface SessaoValidacaoService {

    Sessao validarEObterSessao(Long id);

    Sessao validarAcao(Long id);

    void verificarDuracao(Double duracao, DuracaoSessaoEnum unidade);

   Sessao validarEObterSessao(Long id,Double duracao, DuracaoSessaoEnum unidade);

}