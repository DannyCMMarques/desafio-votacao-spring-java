package com.crud.demo.web.rest;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.service.AssociadoService;
import com.crud.demo.service.dto.associado.AssociadoRequestDTO;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;
import com.crud.demo.web.rest.utils.UriLocationUtils;
import com.crud.demo.web.rest.utils.annotations.PostSwaggerAnnotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/associado")
@RequiredArgsConstructor
@Tag(name = "Associados", description = "Endpoints para operações com associados")
public class AssociadoController {

    private final AssociadoService associadoService;
   @PostMapping
    @Operation(summary = "Cadastrar um associado")
    @PostSwaggerAnnotation
    public ResponseEntity<AssociadoResponseDTO> cadastrarAssociado(
            @Valid @RequestBody AssociadoRequestDTO associadoRequestDTO) {
        AssociadoResponseDTO associadoCriado = associadoService.cadastrarAssociado(associadoRequestDTO);
        URI location = UriLocationUtils.criarLocationUri("api/v1/associado", associadoCriado.getId());
        return ResponseEntity.created(location).body(associadoCriado);
    }

}
