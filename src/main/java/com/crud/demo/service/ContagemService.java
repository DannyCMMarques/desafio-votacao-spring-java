package com.crud.demo.service;

import com.crud.demo.domain.Sessao;

public interface ContagemService {

    void executar(Sessao sessao);

    Long votosAfavor(Sessao sessao);

    Long votosContra(Sessao sessao);

}