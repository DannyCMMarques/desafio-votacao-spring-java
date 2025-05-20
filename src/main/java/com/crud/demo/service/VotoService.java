package com.crud.demo.service;

import com.crud.demo.service.dto.voto.VotoRequestDTO;
import com.crud.demo.service.dto.voto.VotoResponseDTO;

public interface VotoService {
    VotoResponseDTO criarVoto(VotoRequestDTO votoRequest, Long id);

    void associadoJaVotou(Long idUsuario);
}
