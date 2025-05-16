package com.crud.demo.service;

import com.crud.demo.service.dto.sessao.SessaoIniciadaResponseDTO;

public interface IniciarSessaoService {

    SessaoIniciadaResponseDTO executar(Long idSessao);

}