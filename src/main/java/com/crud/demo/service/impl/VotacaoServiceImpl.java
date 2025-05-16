package com.crud.demo.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.StatusPautaEnum;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.service.SessaoValidacaoService;
import com.crud.demo.service.VotacaoService;
import com.crud.demo.service.VotoService;
import com.crud.demo.service.dto.sessao.SessaoIniciadaResponseDTO;
import com.crud.demo.service.mappers.SessaoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VotacaoServiceImpl implements VotacaoService {

    private final SessaoValidacaoService sessaoValidacao;
    private final VotoService votoService;
    private final SessaoMapper sessaoMapper;
    private final SessaoServiceImpl sessaoService;

    @Override
    public SessaoIniciadaResponseDTO iniciarVotacao(Long idSessao) {
        Sessao sessao = sessaoValidacao.validarAcao(idSessao);

        Pauta pauta = sessao.getPauta();

        pauta.setStatus(StatusPautaEnum.EM_VOTACAO);

        sessao.setStatus(StatusSessaoEnum.EM_ANDAMENTO);

        LocalDateTime horarioAtual = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        sessao.setHorarioInicio(horarioAtual);

        SessaoIniciadaResponseDTO sessaoAtualizada = sessaoService.atualizarStatusSessao(sessao);

        return sessaoAtualizada;

    }
}
