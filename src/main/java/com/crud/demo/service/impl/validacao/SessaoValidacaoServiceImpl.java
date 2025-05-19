package com.crud.demo.service.impl.validacao;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.DuracaoSessaoEnum;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.exceptions.sessao.DuracaoMinimaException;
import com.crud.demo.exceptions.sessao.SessaoJaIniciadaException;
import com.crud.demo.exceptions.sessao.SessaoNaoCadastradaException;
import com.crud.demo.repositories.SessaoRepository;
import com.crud.demo.service.validacoes.SessaoValidacaoService;

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

@Override
  public void verificarDuracao(Double duracao, DuracaoSessaoEnum unidade){
boolean eh30Segundos = duracao < 30 && unidade == DuracaoSessaoEnum.SEG;
boolean ehMeioMinuto = duracao < 0.5 && unidade == DuracaoSessaoEnum.MIN;
   if(eh30Segundos || ehMeioMinuto){
    throw new DuracaoMinimaException();
   }
  }
  @Override
  public Sessao validarEObterSessao(Long id,Double duracao, DuracaoSessaoEnum unidade) {
    this.verificarDuracao(duracao,unidade);
    return sessaoRepository.findById(id)
        .orElseThrow(SessaoNaoCadastradaException::new);
  }
}
