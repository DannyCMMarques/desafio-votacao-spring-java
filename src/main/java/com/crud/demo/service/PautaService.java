package com.crud.demo.service;

import org.springframework.data.domain.Page;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.enums.StatusPautaEnum;
import com.crud.demo.service.dto.pauta.PautaRequestDTO;
import com.crud.demo.service.dto.pauta.PautaResponseDTO;

public interface PautaService {

    PautaResponseDTO criarPauta(PautaRequestDTO pautaRequestDTO);

    PautaResponseDTO atualizarPauta(Long id, PautaRequestDTO dto);

    void deletarPauta(Long id);

Page<PautaResponseDTO> listarPautas(int page, int size, String sortBy, String direction, String titulo, StatusPautaEnum status);

    PautaResponseDTO buscarPorId(Long id);

    Pauta buscarPautaNaoVotadaPorId(Long id);

}