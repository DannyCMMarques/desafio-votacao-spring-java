package com.crud.demo.exceptions.pauta;

import org.springframework.http.HttpStatus;

import com.crud.demo.exceptions.ApiException;
import com.crud.demo.exceptions.enums.MensagemExceptionEnum;

public class PautaNaoCadastradaException extends ApiException {

public PautaNaoCadastradaException(){
    super(MensagemExceptionEnum.PAUTA_NAO_CADASTRADA.getMensagem(),HttpStatus.NOT_FOUND);
}
}
