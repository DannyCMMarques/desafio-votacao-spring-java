package com.crud.demo.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.service.dto.sessao.SessaoIniciadaResponseDTO;
import com.crud.demo.service.dto.sessao.SessaoRequestDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;
import com.crud.demo.service.mappers.VotoMapper;
import com.crud.demo.service.mappers.PautaMapper;

@Mapper(componentModel = "spring", uses = { VotoMapper.class, PautaMapper.class })
public interface SessaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "horarioInicio", ignore = true)
    @Mapping(target = "horarioFim", ignore = true)
    @Mapping(target = "votos", ignore = true)
    @Mapping(target = "pauta", source = "pauta")
    @Mapping(target = "status", ignore = true)
    Sessao toEntity(SessaoRequestDTO dto, Pauta pauta);

    @Mapping(target = "pauta", source = "pauta", qualifiedByName = "toResultadoDTO")
    SessaoResponseDTO toResponseDTO(Sessao sessao);

    @Mapping(target = "pauta", source = "pauta", qualifiedByName = "toResultadoDTO")
    @Mapping(target = "votos", source = "votos")
    SessaoIniciadaResponseDTO toIniciadaResponseDTO(Sessao sessao);

    Sessao toEntity(SessaoResponseDTO dto);

    default SessaoResponseDTO toDto(Sessao sessao) {
        if (sessao.getStatus() == StatusSessaoEnum.NAO_INICIADA) {
            return toResponseDTO(sessao);
        }
        return toIniciadaResponseDTO(sessao);
    }
}