package com.crud.demo.exceptions.associado;

import org.springframework.http.HttpStatus;

import com.crud.demo.exceptions.ApiException;
import com.crud.demo.exceptions.enums.MensagemExceptionEnum;

public class CPFJaCadastradoException extends ApiException {

    public CPFJaCadastradoException() {
        super(MensagemExceptionEnum.CPF_DUPLICADO.getMensagem(), HttpStatus.CONFLICT);
    }

}
