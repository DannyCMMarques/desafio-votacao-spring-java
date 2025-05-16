package com.crud.demo.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.enums.StatusPautaEnum;
import com.crud.demo.service.dto.pauta.PautaRequestDTO;
import com.crud.demo.service.dto.pauta.PautaResponseDTO;
import com.crud.demo.service.dto.pauta.PautaResultadoDTO;

@Mapper(componentModel = "spring")
public interface PautaMapper {

  Pauta toEntity(PautaRequestDTO pauta);

  Pauta toEntity(PautaResponseDTO pautaResponse);

  PautaResponseDTO toResponseDto(Pauta pauta);

  @Named("toResultadoDTO")
  PautaResultadoDTO toResultadoDTO(Pauta pauta);

  default PautaResponseDTO toDto(Pauta pauta) {
    if (pauta.getStatus() == StatusPautaEnum.NAO_VOTADA) {
      return toResponseDto(pauta);
    }
    return toResultadoDTO(pauta);
  }

}
