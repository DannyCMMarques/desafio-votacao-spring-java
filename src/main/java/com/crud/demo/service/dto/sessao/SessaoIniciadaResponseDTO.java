package com.crud.demo.service.dto.sessao;

import java.time.LocalDateTime;
import java.util.List;

import com.crud.demo.service.dto.pauta.PautaResultadoDTO;
import com.crud.demo.service.dto.voto.VotoResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class SessaoIniciadaResponseDTO extends SessaoResponseDTO {

  @Schema(description = "Pauta associada à sessão")
  private PautaResultadoDTO pauta;

  @Schema(description = "Horário de início da sessão", example = "2023-01-01T10:00:00")
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime horarioInicio;

  @Schema(description = "Horário de início da sessão", example = "2023-01-01T10:05:00")
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime horarioFim;

  @Schema(description = "Lista de votos registrados na sessão")
  private List<VotoResponseDTO> votos;
}
