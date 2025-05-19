package com.crud.demo.exceptions.sessao;

import org.springframework.http.HttpStatus;

import com.crud.demo.exceptions.ApiException;
import com.crud.demo.exceptions.enums.MensagemExceptionEnum;

public class SessaoJaIniciadaException extends ApiException {

    public SessaoJaIniciadaException(){
        super(MensagemExceptionEnum.SESSAO_JA_INICIADA.getMensagem(),HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
