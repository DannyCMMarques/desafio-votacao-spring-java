package com.crud.demo.service.impl;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.repositories.VotoRepository;
import com.crud.demo.service.ContagemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContagemServiceImpl implements ContagemService {

    private final PautaRepository pautaRepository;
    private final VotoRepository votoRepository;

    @Override
    public void executar(Sessao sessao) {
        Pauta pauta = sessao.getPauta();
        Long votosFavor = this.votosAfavor(sessao);
        Long votosContra = this.votosContra(sessao);
        log.info("voto a favor {}",votosFavor);
        log.info("votos contra {}",votosContra);
        pauta.setVotosFavor(votosFavor);
        pauta.setVotosContra(votosContra);
        pautaRepository.save(pauta);
    }
@Override
public Long votosAfavor(Sessao sessao) {
    return votoRepository.countBySessaoAndVoto(sessao, true);
}

@Override
public Long votosContra(Sessao sessao) {
    return votoRepository.countBySessaoAndVoto(sessao, false);
}
}
