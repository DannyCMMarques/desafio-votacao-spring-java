package com.crud.demo.service.dto.pauta;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PautaRequestDTO {
 
    @Schema(description = "Título da pauta", example = "Revisão do Estatuto")
    private String titulo;

    @Schema(description = "Descrição detalhada da pauta", example = "Proposta de atualização dos artigos do estatuto da associação.")
    private String descricao;
}
