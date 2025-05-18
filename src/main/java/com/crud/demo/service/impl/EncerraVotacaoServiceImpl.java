package com.crud.demo.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.repositories.SessaoRepository;
import com.crud.demo.service.EncerraVotacaoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncerraVotacaoServiceImpl implements EncerraVotacaoService {

    public final SessaoRepository sessaoRepository;
    public final PautaRepository pautaRepository;

    @Override
    public void finalizarSessao(Long idSessao) {
        Sessao sessao = sessaoRepository.findById(idSessao).get();
        Pauta pauta = sessao.getPauta();
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        LocalDateTime horarioAtual = LocalDateTime.now(zoneId);
        pauta.finalizarVotacaoPauta();
        sessao.finalizarSessao(horarioAtual);
        sessaoRepository.save(sessao);
        pautaRepository.save(pauta);

        log.info("ENCERREI {}", idSessao);
        log.info("ENCERREI Pauta {}", pauta.getId());

    }
}
