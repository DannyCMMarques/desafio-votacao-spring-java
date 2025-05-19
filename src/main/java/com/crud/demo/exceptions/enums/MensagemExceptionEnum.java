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
    SESSAO_NAO_CADASTRADA("Sessão não encontrada com ID informado"),
    ERRO_INTERNO("Erro interno no servidor."),
    SESSAO_JA_INICIADA("Essa ação não é possível ser realizada, pois, a sessão já foi iniciada"),
    SESSAO_NAO_INICIADA("Essa ação não é possível ser realizada, pois, a sessão ainda não foi inicializada"),
    DURACAO_MIN("A duração minima de uma sessão é de 30 segundos");
    private final String mensagem;

}
