package com.crud.demo.service.dto.associado;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AssociadoResponseDTO {

    @Schema(description = "Id do associado", example = "1L") 
    Long id;
    @Schema(description = "Nome completo do associado", example = "João da Silva")
    String nome;
}
