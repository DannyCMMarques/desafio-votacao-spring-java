package com.crud.demo.service.impl;

import org.springframework.stereotype.Service;

import com.crud.demo.domain.Associado;
import com.crud.demo.repositories.AssociadoRepository;
import com.crud.demo.service.AssociadoService;
import com.crud.demo.service.AssociadoValidacaoService;
import com.crud.demo.service.dto.associado.AssociadoRequestDTO;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;
import com.crud.demo.service.mappers.AssociadoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssociadoServiceImpl implements AssociadoService {

    private final AssociadoRepository associadoRepository;
    private final AssociadoValidacaoService associadoValidacaoService;
    private final AssociadoMapper associadoMapper;

    @Override
    public AssociadoResponseDTO cadastrarAssociado(AssociadoRequestDTO associadoRequestDTO) {

        associadoValidacaoService.validarExistenciaCPF(associadoRequestDTO.getCpf());

        Associado associadoEntity = associadoMapper.toEntity(associadoRequestDTO);

        Associado associadoSalvo = associadoRepository.save(associadoEntity);

        AssociadoResponseDTO associadoResponse = associadoMapper.toDTO(associadoSalvo);

        return associadoResponse;

    }

}
