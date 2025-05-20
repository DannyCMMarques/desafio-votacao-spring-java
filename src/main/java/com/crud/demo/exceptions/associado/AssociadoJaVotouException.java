package com.crud.demo.exceptions.associado;

import org.springframework.http.HttpStatus;

import com.crud.demo.exceptions.ApiException;
import com.crud.demo.exceptions.enums.MensagemExceptionEnum;

public class AssociadoJaVotouException extends ApiException {

    public AssociadoJaVotouException() {
        super(MensagemExceptionEnum.NAO_EXCLUIR_ASSOCIADO.getMensagem(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
