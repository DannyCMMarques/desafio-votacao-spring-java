package com.crud.demo.service.dto.voto;

import com.crud.demo.domain.Associado;
import com.crud.demo.domain.enums.VotoEnum;

import io.swagger.v3.oas.annotations.media.Schema;

public class VotoResponseDTO {

@Schema(description = "ID do voto registrado.", example = "1")
private Long id;

@Schema(description = "Indica o voto do associado. Pode ser SIM ou NÃO.", example = "SIM")
private VotoEnum voto;

@Schema(description = "ID da sessão onde o voto foi registrado.", example = "1")
private Long sessao;

@Schema(description = "Dados do associado que realizou o voto.")
private Associado associado;
}
