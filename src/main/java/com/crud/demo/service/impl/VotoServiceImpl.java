package com.crud.demo.service.impl;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Associado;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.Voto;
import com.crud.demo.repositories.VotoRepository;
import com.crud.demo.service.AssociadoValidacaoService;
import com.crud.demo.service.SessaoValidacaoService;
import com.crud.demo.service.VotoService;
import com.crud.demo.service.VotoValidacaoService;
import com.crud.demo.service.dto.voto.VotoRequestDTO;
import com.crud.demo.service.dto.voto.VotoResponseDTO;
import com.crud.demo.service.mappers.VotoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VotoServiceImpl implements VotoService {

    private final VotoRepository votoRepository;
    private final VotoMapper votoMapper;
    private final SessaoValidacaoService sessaoValidacao;
    private final AssociadoValidacaoService associadoValidacaoService;
    private final VotoValidacaoService votoValidacaoService;

    public VotoResponseDTO criarVoto(VotoRequestDTO votoRequest, Long idSessao) {
        Sessao sessao = sessaoValidacao.validarEObterSessao(idSessao);
        Long idAssociado = votoRequest.getAssociado();
        Associado associado = associadoValidacaoService.validarExistencia(idAssociado);
        Voto votoEntity = votoMapper.toEntity(votoRequest, sessao, associado);
        Voto votoCriado = votoRepository.save(votoEntity);
        VotoResponseDTO votoResponse = votoMapper.toDTO(votoCriado);
        return votoResponse;
    }
}
