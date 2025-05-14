package com.crud.demo.service.mappers;

import org.mapstruct.Mapper;

import com.crud.demo.domain.Pauta;
import com.crud.demo.service.dto.pauta.PautaRequestDTO;
import com.crud.demo.service.dto.pauta.PautaResponseDTO;
import com.crud.demo.service.dto.pauta.PautaResultadoDTO;

@Mapper(componentModel = "spring")
public interface PautaMapper {

  Pauta toEntity(PautaRequestDTO pauta);

  PautaResponseDTO toDto(Pauta pauta);

  PautaResultadoDTO toDTO(Pauta pauta);

}
