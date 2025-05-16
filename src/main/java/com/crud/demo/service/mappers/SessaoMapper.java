package com.crud.demo.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.service.dto.sessao.SessaoIniciadaResponseDTO;
import com.crud.demo.service.dto.sessao.SessaoRequestDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;

@Mapper(componentModel = "spring", uses = VotoMapper.class)
public interface SessaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "horarioInicio", ignore = true)
    @Mapping(target = "horarioFim", ignore = true)
    @Mapping(target = "votos", ignore = true)
    @Mapping(target = "pauta", source = "pauta")
    @Mapping(target = "status", ignore = true)
    Sessao toEntity(SessaoRequestDTO dto, Pauta pauta);

    SessaoResponseDTO toResponseDTO(Sessao sessao);

    SessaoIniciadaResponseDTO toIniciadaResponseDTO(Sessao sessao);

    Sessao toEntity(SessaoResponseDTO dto);
}