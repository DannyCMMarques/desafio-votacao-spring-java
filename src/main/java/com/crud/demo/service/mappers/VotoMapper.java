package com.crud.demo.service.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.crud.demo.domain.Associado;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.Voto;
import com.crud.demo.service.dto.voto.VotoRequestDTO;
import com.crud.demo.service.dto.voto.VotoResponseDTO;

@Mapper(componentModel = "spring")
public interface  VotoMapper {


    @Mapping(target = "id",        ignore = true)
    @Mapping(target = "sessao",    expression = "java(sessao)")
    @Mapping(target = "associado", expression = "java(associado)")
    @Mapping(target = "voto",      source = "dto.voto")
    Voto toEntity(VotoRequestDTO dto, Sessao sessao, Associado associado);


    VotoResponseDTO toDTO(Voto voto);
}
