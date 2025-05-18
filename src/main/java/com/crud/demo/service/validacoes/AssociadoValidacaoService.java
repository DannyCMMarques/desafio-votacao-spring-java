package com.crud.demo.service.validacoes;

import com.crud.demo.domain.Associado;

public interface AssociadoValidacaoService {

    void validarCPFCadastro(String cpf);

    Associado validarExistencia(Long id);

    void validarCPFAtualizacao(String cpf,Long id);
}
