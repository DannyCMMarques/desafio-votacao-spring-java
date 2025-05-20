package com.crud.demo.service;

import org.springframework.data.domain.Page;

import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.DuracaoSessaoEnum;
import com.crud.demo.service.dto.sessao.SessaoRequestDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;

public interface SessaoService {

    SessaoResponseDTO criarSessao(SessaoRequestDTO dto);

    Page<SessaoResponseDTO> listarSessoes(int page, int size, String sortBy, String direction);

    SessaoResponseDTO buscarPorId(Long id);

    SessaoResponseDTO atualizarSessao(Long id, SessaoRequestDTO dto);

    void deletarSessao(Long id);

    void verificarDuracao(Double duracao, DuracaoSessaoEnum unidade);

    Sessao verificarSessaoJaIniciada(Long id);


    Sessao verificarSessaoAberta(Long id);
}