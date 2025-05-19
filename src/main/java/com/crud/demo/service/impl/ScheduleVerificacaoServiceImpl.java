package com.crud.demo.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.crud.demo.repositories.SessaoRepository;
import com.crud.demo.service.EncerraVotacaoService;
import com.crud.demo.service.ScheduleVerificacaoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableAsync
@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleVerificacaoServiceImpl implements ScheduleVerificacaoService {
  private final SessaoRepository sessaoRepository;
  private final EncerraVotacaoService encerraVotacaoService;

  @Override
  @Async
  @Scheduled(fixedRateString = "${sessao.verificacao.fixed-rate}")
  public void verificarDuracao() {
    log.info("Iniciando verificação");
    ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
    LocalDateTime horarioAtual = LocalDateTime.now(zoneId);
    List<Long> vencidas = sessaoRepository.findIdsVencidas(horarioAtual);
    vencidas.parallelStream()
        .forEach(encerraVotacaoService::finalizarSessao);
    log.info("Encerrando sessões com id {}", horarioAtual);
  }

}
