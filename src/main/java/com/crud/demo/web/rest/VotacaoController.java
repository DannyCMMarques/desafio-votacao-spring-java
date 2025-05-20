package com.crud.demo.web.rest;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.service.VotoService;
import com.crud.demo.service.dto.voto.VotoRequestDTO;
import com.crud.demo.service.dto.voto.VotoResponseDTO;
import com.crud.demo.web.rest.utils.UriLocationUtils;
import com.crud.demo.web.rest.utils.annotations.PostSwaggerAnnotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sessao")
@RequiredArgsConstructor
@Tag(name = "Votação", description = "Endpoints para votação")
public class VotacaoController {

    private final VotoService votoService;

    @PostMapping("/{id}/votar")
    @Operation(summary = "Votar")
    @PostSwaggerAnnotation
    public ResponseEntity<VotoResponseDTO> votar(
            @Parameter(description = "ID da sessão em que o voto será registrado") @PathVariable("id") Long sessaoId,
            @Valid @RequestBody VotoRequestDTO votoRequestDTO) {
        VotoResponseDTO votoRealizado = votoService.criarVoto(votoRequestDTO, sessaoId);
        URI location = UriLocationUtils.criarLocationUri("api/v1/sessao", votoRealizado.getId());
        return ResponseEntity.created(location).body(votoRealizado);
    }

}
