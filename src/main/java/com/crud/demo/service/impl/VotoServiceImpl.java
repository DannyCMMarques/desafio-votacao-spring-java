package com.crud.demo.service.impl;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Associado;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.Voto;
import com.crud.demo.exceptions.VotoDuplicadoException;
import com.crud.demo.exceptions.associado.AssociadoJaVotouException;
import com.crud.demo.exceptions.associado.AssociadoNaoEncontradoException;
import com.crud.demo.repositories.VotoRepository;
import com.crud.demo.repositories.AssociadoRepository;
import com.crud.demo.service.ContagemService;
import com.crud.demo.service.SessaoService;
import com.crud.demo.service.VotoService;
import com.crud.demo.service.dto.voto.VotoRequestDTO;
import com.crud.demo.service.dto.voto.VotoResponseDTO;
import com.crud.demo.service.mappers.VotoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VotoServiceImpl implements VotoService {

    private final VotoRepository votoRepository;
    private final VotoMapper votoMapper;
    private final SessaoService sessaoService;
    private final ContagemService contagemService;
    private final AssociadoRepository associadoRepository;

    @Override
    public VotoResponseDTO criarVoto(VotoRequestDTO votoRequest, Long idSessao) {

        Sessao sessao = sessaoService.verificarSessaoAberta(idSessao);
        Long idAssociado = votoRequest.getAssociado();

        Associado associado = associadoRepository.findById(idAssociado)
                .orElseThrow(AssociadoNaoEncontradoException::new);
        this.verificarVotoDuplicado(sessao, associado);
        Voto votoEntity = votoMapper.toEntity(votoRequest, sessao, associado);
        Voto votoCriado = votoRepository.save(votoEntity);
        contagemService.executar(sessao);
        VotoResponseDTO votoResponse = votoMapper.toDTO(votoCriado);
        return votoResponse;
    }

    private void verificarVotoDuplicado(Sessao sessao, Associado associado) {
        boolean jaVotou = sessao.getVotos().stream()
                .anyMatch(voto -> voto.getAssociado().equals(associado));
        if (jaVotou) {
            throw new VotoDuplicadoException();
        }
    }

    @Override
    public void associadoJaVotou(Long idUsuario) {
        if (votoRepository.existsByAssociadoId(idUsuario)) {
            throw new AssociadoJaVotouException();
        }
    }
}
