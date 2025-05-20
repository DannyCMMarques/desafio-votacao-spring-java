package com.crud.demo.exceptions;

import org.springframework.http.HttpStatus;

import com.crud.demo.exceptions.enums.MensagemExceptionEnum;

public class VotoDuplicadoException extends ApiException{
    public VotoDuplicadoException(){
        super(MensagemExceptionEnum.VOTO_DUPLICADO.getMensagem(), HttpStatus.CONFLICT);
    }

}
