package com.crud.demo.exceptions.sessao;

import org.springframework.http.HttpStatus;

import com.crud.demo.exceptions.ApiException;
import com.crud.demo.exceptions.enums.MensagemExceptionEnum;

public class SessaoNaoCadastradaException extends ApiException {
    public SessaoNaoCadastradaException() {
        super(MensagemExceptionEnum.SESSAO_NAO_CADASTRADA.getMensagem(), HttpStatus.NOT_FOUND);
    }
}
