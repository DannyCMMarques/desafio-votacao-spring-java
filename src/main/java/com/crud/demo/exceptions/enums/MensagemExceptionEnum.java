package com.crud.demo.exceptions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MensagemExceptionEnum {
    PAUTA_NAO_CADASTRADA("Pauta não encontrada com o ID informado."),
    PAUTA_JA_VOTADA("Essa operação só é permitida em pautas ainda não votadas."),
    ASSOCIADO_NAO_ENCONTRADO("Associado não encontrado."),
    CPF_DUPLICADO("Já existe um associado com esse CPF cadastrado."),
    SESSAO_EXPIRADA("A sessão de votação já foi encerrada."),
    VOTO_DUPLICADO("Esse associado já votou nessa pauta."),
    ERRO_INTERNO("Erro interno no servidor.");

    private final String mensagem;

}
