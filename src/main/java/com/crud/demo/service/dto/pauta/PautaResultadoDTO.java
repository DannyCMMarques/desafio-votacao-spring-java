package com.crud.demo.service.dto.pauta;

import com.crud.demo.domain.enums.ResultadoPautaEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PautaResultadoDTO extends PautaResponseDTO {

    @Schema(description = "Quantidade de votos contra a pauta", example = "10")
    private Integer votosContra;

    @Schema(description = "Quantidade de votos a favor da pauta", example = "25")
    private Integer votosFavor;

    @Schema(description = "Quantidade total de votos na pauta", example = "35")
    private Integer votosTotais;

    @Schema(description = "Resultado final da pauta", example = "APROVADo")
    private ResultadoPautaEnum resultado;

}
