package com.crud.demo.exceptions.sessao;

import org.springframework.http.HttpStatus;

import com.crud.demo.exceptions.ApiException;
import com.crud.demo.exceptions.enums.MensagemExceptionEnum;

public class SessaoNaoIniciadaException extends ApiException {

    public SessaoNaoIniciadaException(){
        super(MensagemExceptionEnum.SESSAO_NAO_INICIADA.getMensagem(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
