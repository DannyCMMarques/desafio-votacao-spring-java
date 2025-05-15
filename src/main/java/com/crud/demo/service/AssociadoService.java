package com.crud.demo.service;

import org.springframework.data.domain.Page;

import com.crud.demo.service.dto.associado.AssociadoRequestDTO;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;

public interface AssociadoService {

    AssociadoResponseDTO cadastrarAssociado(AssociadoRequestDTO associadoRequestDTO);

    AssociadoResponseDTO buscarAssociadoPorId(Long id);

    Page<AssociadoResponseDTO> listarTodosAssociados(int page, int size, String sortBy, String direction);

    AssociadoResponseDTO atualizarAssociado(Long id, AssociadoRequestDTO request);

    void deletarAssociado(Long id);
}