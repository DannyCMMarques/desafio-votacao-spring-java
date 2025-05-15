package com.crud.demo.service.dto.pauta;

import com.crud.demo.domain.enums.StatusPautaEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PautaResponseDTO {
    @Schema(description = "Id da Pauta")
    Long id;
    @Schema(description = "Título da pauta", example = "Revisão do Estatuto")
    private String titulo;

    @Schema(description = "Descrição detalhada da pauta")
    private String descricao;

    @Schema(description = "Status atual da pauta", example = "NAO_VOTADA")
    private StatusPautaEnum status;
}
