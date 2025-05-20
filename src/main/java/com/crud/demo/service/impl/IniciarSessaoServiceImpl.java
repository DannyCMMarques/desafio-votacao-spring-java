package com.crud.demo.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.repositories.SessaoRepository;
import com.crud.demo.service.IniciarSessaoService;
import com.crud.demo.service.SessaoService;
import com.crud.demo.service.dto.sessao.SessaoIniciadaResponseDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;
import com.crud.demo.service.mappers.SessaoMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IniciarSessaoServiceImpl implements IniciarSessaoService {

    private final SessaoMapper sessaoMapper;
    private final SessaoService sessaoService;

    private final PautaRepository pautaRepository;
    private final SessaoRepository sessaoRepository;

    @Override
    @Transactional
    public SessaoIniciadaResponseDTO executar(Long idSessao) {
        Sessao sessao = this.iniciarSessao(idSessao);
        this.iniciarPauta(sessao);
        SessaoIniciadaResponseDTO sessaoAtualizada = sessaoMapper.toIniciadaResponseDTO(sessao);
        return sessaoAtualizada;
    }

    public void iniciarPauta(Sessao sessao) {
        Pauta pauta = sessao.getPauta();
        pauta.iniciarVotacaoPauta();
        pautaRepository.save(pauta);
    }

    public Sessao iniciarSessao(Long idSessao) {
        SessaoResponseDTO sessao = sessaoService.buscarPorId(idSessao);
        Sessao sessaoEntity = sessaoMapper.toEntity(sessao);
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        LocalDateTime horarioAtual = LocalDateTime.now(zoneId);
        sessaoEntity.iniciarSessao(horarioAtual);
        Sessao sessaoInicializada = sessaoRepository.save(sessaoEntity);
        return sessaoInicializada;
    }
}
