package com.crud.demo.exceptions.sessao;

import org.springframework.http.HttpStatus;

import com.crud.demo.exceptions.ApiException;
import com.crud.demo.exceptions.enums.MensagemExceptionEnum;

public class DuracaoMinimaException extends ApiException {

    public DuracaoMinimaException() {
        super(MensagemExceptionEnum.DURACAO_MIN.getMensagem(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
