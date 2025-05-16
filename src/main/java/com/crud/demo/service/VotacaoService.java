package com.crud.demo.service;

import com.crud.demo.service.dto.sessao.SessaoIniciadaResponseDTO;
import com.crud.demo.service.dto.voto.VotoRequestDTO;

public interface VotacaoService {

    SessaoIniciadaResponseDTO iniciarVotacao(Long idSessao);

}