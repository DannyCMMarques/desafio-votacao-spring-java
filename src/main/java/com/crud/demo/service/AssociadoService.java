package com.crud.demo.service;

import com.crud.demo.service.dto.associado.AssociadoRequestDTO;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;

public interface AssociadoService {

    AssociadoResponseDTO cadastrarAssociado(AssociadoRequestDTO associadoRequestDTO);

}