package com.crud.demo.service.impl.validacao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.enums.StatusPautaEnum;
import com.crud.demo.exceptions.pauta.PautaNaoCadastradaException;
import com.crud.demo.exceptions.pauta.PautaVotadaException;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.service.validacoes.PautaValidacaoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PautaValidacaoServiceImpl implements PautaValidacaoService {

  private final PautaRepository pautaRepository;

    @Override
  public Pauta verificarStatusNaoVotada(Long id) {
    Pauta pautaEncontrada = this.validarEObterPauta(id);

    List<Pauta> pautasNaoIniciadas = pautaRepository.findAllByStatus(StatusPautaEnum.NAO_VOTADA);
    if (!pautasNaoIniciadas.contains(pautaEncontrada)) {
      throw new PautaVotadaException();
    }
    return pautaEncontrada;
  }

    @Override
  public Pauta validarEObterPauta(Long id) {
    return pautaRepository.findById(id)
        .orElseThrow(PautaNaoCadastradaException::new);
  }
}
