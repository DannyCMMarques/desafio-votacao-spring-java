package com.crud.demo.exceptions.associado;

import org.springframework.http.HttpStatus;

import com.crud.demo.exceptions.ApiException;

public class CPFJaCadastradoException extends ApiException {

    public CPFJaCadastradoException(){
        super("Já existe um associado cadastrado com esse CPF",HttpStatus.CONFLICT);
    }

}
