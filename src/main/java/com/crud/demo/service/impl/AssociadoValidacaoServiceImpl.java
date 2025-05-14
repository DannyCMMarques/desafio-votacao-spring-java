package com.crud.demo.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Associado;
import com.crud.demo.exceptions.associado.CPFJaCadastradoException;
import com.crud.demo.repositories.AssociadoRepository;
import com.crud.demo.service.AssociadoValidacaoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssociadoValidacaoServiceImpl implements AssociadoValidacaoService {

private final AssociadoRepository associadoRepository;

    @Override
    public void validarExistenciaCPF(String cpf){
         Optional<Associado> associadoExistente =  associadoRepository.findByCpf(cpf);

         if(associadoExistente.isPresent()){
            throw new CPFJaCadastradoException();
         }

    }

}
