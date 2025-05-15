package com.crud.demo.exceptions.associado;

import org.springframework.http.HttpStatus;

import com.crud.demo.exceptions.ApiException;
import com.crud.demo.exceptions.enums.MensagemExceptionEnum;

public class AssociadoNaoEncontradoException extends ApiException {
public AssociadoNaoEncontradoException(){
super(MensagemExceptionEnum.ASSOCIADO_NAO_ENCONTRADO.getMensagem(),HttpStatus.NOT_FOUND);
}
}
