package com.crud.demo.service;

import org.springframework.data.domain.Page;

import com.crud.demo.domain.Pauta;
import com.crud.demo.service.dto.pauta.PautaRequestDTO;
import com.crud.demo.service.dto.pauta.PautaResponseDTO;
import com.crud.demo.service.dto.pauta.PautaResultadoDTO;

public interface PautaService {

    PautaResponseDTO criarPauta(PautaRequestDTO pautaRequestDTO);

    PautaResponseDTO atualizarPauta(Long id, PautaRequestDTO dto);

    void deletarPauta(Long id);

    Page<PautaResponseDTO> listarPautas(int page, int size, String sortBy, String direction);

    public PautaResultadoDTO atualizarPauta(Pauta pauta);

    PautaResponseDTO buscarPorId(Long id);

}