package com.crud.demo.service.dto.voto;

import com.crud.demo.domain.enums.VotoEnum;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class VotoResponseDTO {

    @Schema(description = "ID do voto registrado.", example = "1")
    private Long id;

    @Schema(description = "Indica o voto do associado. Pode ser SIM ou NÃO.", example = "SIM")
    private VotoEnum voto;

    @Schema(description = "Dados do associado que realizou o voto.")
    private AssociadoResponseDTO associado;

}
