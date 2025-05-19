package com.crud.demo.exceptions.sessao;

import org.springframework.http.HttpStatus;

import com.crud.demo.exceptions.ApiException;
import com.crud.demo.exceptions.enums.MensagemExceptionEnum;

public class SessaoJaFinalizadaException extends ApiException {

    public SessaoJaFinalizadaException() {
        super(MensagemExceptionEnum.SESSAO_EXPIRADA.getMensagem(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
