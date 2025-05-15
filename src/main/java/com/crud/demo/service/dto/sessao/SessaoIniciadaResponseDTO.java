package com.crud.demo.service.dto.sessao;

import java.time.LocalDateTime;
import java.util.List;

import com.crud.demo.service.dto.voto.VotoResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessaoIniciadaResponseDTO extends SessaoResponseDTO {
    @Schema(description = "Horário de início da sessão", example = "2023-01-01T10:00:00")
    private LocalDateTime horarioInicio;
    @Schema(description = "Horário de início da sessão", example = "2023-01-01T10:05:00")
    private LocalDateTime horarioFim;
    @Schema(description = "Lista de votos registrados na sessão")
    private List<VotoResponseDTO> votos;
}
