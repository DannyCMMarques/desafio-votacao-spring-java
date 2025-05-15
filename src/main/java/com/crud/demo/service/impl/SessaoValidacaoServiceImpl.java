package com.crud.demo.service.impl;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.exceptions.sessao.SessaoJaIniciadaException;
import com.crud.demo.exceptions.sessao.SessaoNaoCadastradaException;
import com.crud.demo.repositories.SessaoRepository;
import com.crud.demo.service.SessaoValidacaoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessaoValidacaoServiceImpl implements SessaoValidacaoService {

  private final SessaoRepository sessaoRepository;

  @Override
  public Sessao validarEObterSessao(Long id) {
    return sessaoRepository.findById(id)
        .orElseThrow(SessaoNaoCadastradaException::new);
  }

  @Override
  public Sessao validarAcao(Long id) {
    Sessao sessaoEncontrada = this.validarEObterSessao(id);
    if (sessaoEncontrada.getStatus() != StatusSessaoEnum.NAO_INICIADA) {
      throw new SessaoJaIniciadaException();
    }
    return sessaoEncontrada;
  }
}
