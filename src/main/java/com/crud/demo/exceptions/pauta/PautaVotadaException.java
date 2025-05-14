package com.crud.demo.exceptions.pauta;

import org.springframework.http.HttpStatus;

import com.crud.demo.exceptions.ApiException;
import com.crud.demo.exceptions.enums.MensagemExceptionEnum;

public class PautaVotadaException extends ApiException {

    public PautaVotadaException (){
        super(MensagemExceptionEnum.PAUTA_JA_VOTADA.getMensagem(),HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
