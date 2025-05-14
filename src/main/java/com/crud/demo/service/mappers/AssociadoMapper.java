package com.crud.demo.service.mappers;

import org.mapstruct.Mapper;

import com.crud.demo.domain.Associado;
import com.crud.demo.service.dto.associado.AssociadoRequestDTO;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;

@Mapper(componentModel = "spring")
public interface AssociadoMapper {

    Associado toEntity(AssociadoRequestDTO associadoRequestDTO);

    AssociadoResponseDTO toDTO(Associado associado);

    Associado toEntity(AssociadoResponseDTO associadoResponseDTO);

}
