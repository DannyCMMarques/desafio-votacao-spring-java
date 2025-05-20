package com.crud.demo.service.dto.voto;

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
public class VotoRequestDTO {

    @Schema(description = "Indica o voto do associado. Deve ser true para SIM ou false para NÃO.", example = "true")
    @NotNull
    private Boolean voto;

    @Schema(description = "ID do associado que está votando.", example = "42")
    @NotNull
    private Long associado;


}
