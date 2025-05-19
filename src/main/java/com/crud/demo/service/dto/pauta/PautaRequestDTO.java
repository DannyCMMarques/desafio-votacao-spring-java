package com.crud.demo.service.dto.pauta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PautaRequestDTO {
    @NotBlank
    @Size(max = 100)
    @Schema(description = "Título da pauta", example = "Revisão do Estatuto")
    private String titulo;

    @NotBlank
    @Size(max = 500)
    @Schema(description = "Descrição detalhada da pauta", example = "Proposta de atualização dos artigos do estatuto da associação.")
    private String descricao;
}
