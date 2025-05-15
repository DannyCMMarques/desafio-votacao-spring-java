package com.crud.demo.service.dto.sessao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessaoRequestDTO {

    @NotNull
    @Schema(description = "Id da Pauta selecionada para ser votada", example = "1")
    private Long idPauta;

    @NotNull
    @Schema(description = "Tempo de duração da sessão em minutos", example = "1.5")
    private Integer duracao;

}
