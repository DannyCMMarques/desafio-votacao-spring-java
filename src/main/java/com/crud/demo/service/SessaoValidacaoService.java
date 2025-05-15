package com.crud.demo.service;

import com.crud.demo.domain.Sessao;

public interface SessaoValidacaoService {

    Sessao validarEObterSessao(Long id);

    Sessao validarAcao(Long id);

}