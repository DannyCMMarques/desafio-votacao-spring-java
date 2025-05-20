package com.crud.demo.service.impl;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.service.ContagemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContagemServiceImpl implements ContagemService {

    private final PautaRepository pautaRepository;

    @Override
    public void executar(Sessao sessao) {
        Pauta pauta = sessao.getPauta();
        Long votosFavor = this.votosAfavor(sessao);
        Long votosContra = this.votosContra(sessao);
        pauta.setVotosFavor(votosFavor);
        pauta.setVotosContra(votosContra);
        pautaRepository.save(pauta);
    }

    @Override
    public Long votosAfavor(Sessao sessao) {
        Long votosFavoraveis = sessao.getVotos().stream().filter(voto -> voto.getVoto()).count();
        return votosFavoraveis;
    }

    @Override
    public Long votosContra(Sessao sessao) {
        Long votosContra = sessao.getVotos().stream().filter(voto -> voto.getVoto() == false).count();
        return votosContra;
    }

}
