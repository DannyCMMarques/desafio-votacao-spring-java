package com.crud.demo.service.dto.sessao;

import com.crud.demo.domain.enums.DuracaoSessaoEnum;

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

    @Schema(description = "Tempo de duração da sessão", example = "1.5")
    private Double duracao;

    @NotNull()
    @Schema(description = "Unidade de duração da sessao", example = "MIN")
    private DuracaoSessaoEnum unidade;

}
