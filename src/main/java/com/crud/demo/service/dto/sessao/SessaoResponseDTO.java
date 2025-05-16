package com.crud.demo.service.dto.sessao;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.service.dto.pauta.PautaResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class SessaoResponseDTO {
  @Schema(description = "Identificador único da sessão", example = "1")
    private Long id;

  @Schema(description = "Pauta associada à sessão")
    private PautaResponseDTO pauta;

  @Schema(description = "Duração da sessão em minutos", example = "1")
    private Integer duracao ;

  @Schema(description = "Status atual da sessão", example = "NAO_INICIADA")
    private StatusSessaoEnum status = StatusSessaoEnum.NAO_INICIADA;

}
