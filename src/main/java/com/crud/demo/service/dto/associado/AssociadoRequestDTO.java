package com.crud.demo.service.dto.associado;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AssociadoRequestDTO {

    @Schema(description = "Nome completo do associado", example = "João da Silva")
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Schema(description = "CPF do associado sem pontuação", example = "12345678901")
    @NotBlank(message = "O CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos numéricos")
    private String cpf;
}