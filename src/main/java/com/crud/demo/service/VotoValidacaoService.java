package com.crud.demo.service;

import com.crud.demo.domain.Associado;
import com.crud.demo.domain.Sessao;

public interface VotoValidacaoService {

    void validar(Sessao sessao, Associado associado);

}