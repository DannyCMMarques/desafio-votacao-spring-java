package com.crud.demo.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.StatusPautaEnum;
import com.crud.demo.service.SessaoService;
import com.crud.demo.service.SessaoValidacaoService;
import com.crud.demo.service.VotacaoService;
import com.crud.demo.service.VotoService;
import com.crud.demo.service.dto.sessao.SessaoIniciadaResponseDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;
import com.crud.demo.service.mappers.SessaoMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VotacaoServiceImpl implements VotacaoService {

    private final SessaoValidacaoService sessaoValidacao;
    private final VotoService votoService;
    private final SessaoMapper sessaoMapper;
    private final SessaoService sessaoService;

    @Override
    @Transactional
    public SessaoIniciadaResponseDTO iniciarVotacao(Long idSessao) {
        SessaoResponseDTO sessao = sessaoService.buscarPorId(idSessao);
        Pauta pauta = sessao.getPauta();
        pauta.iniciarVotacaoPauta();
        Sessao sessaoEntity = sessaoMapper.toEntity(sessao);
        LocalDateTime horarioAtual = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        sessaoEntity.iniciarSessao(horarioAtual);
        SessaoIniciadaResponseDTO sessaoAtualizada = sessaoMapper.toIniciadaResponseDTO(sessaoEntity);
        return sessaoAtualizada;
    }
}
