package com.crud.demo.web.rest;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.service.PautaService;
import com.crud.demo.service.dto.pauta.PautaRequestDTO;
import com.crud.demo.service.dto.pauta.PautaResponseDTO;
import com.crud.demo.web.rest.utils.UriLocationUtils;
import com.crud.demo.web.rest.utils.annotations.DeleteSwaggerAnnotation;
import com.crud.demo.web.rest.utils.annotations.GetSwaggerAnnotation;
import com.crud.demo.web.rest.utils.annotations.PostSwaggerAnnotation;
import com.crud.demo.web.rest.utils.annotations.PutSwaggerAnnotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/pauta")
@RequiredArgsConstructor
@Tag(name = "Pauta", description = "Endpoints para operações com pautas")
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    @Operation(summary = "Cadastrar uma pauta")
    @PostSwaggerAnnotation
    public ResponseEntity<PautaResponseDTO> cadastrarPauta(
            @Valid @RequestBody PautaRequestDTO PautaRequestDTO) {
        PautaResponseDTO pautaCriada = pautaService.criarPauta(PautaRequestDTO);
        URI location = UriLocationUtils.criarLocationUri("api/v1/pauta", pautaCriada.getId());
        return ResponseEntity.created(location).body(pautaCriada);
    }

    @GetMapping
    @Operation(summary = "Listar todas as pautas")
    @GetSwaggerAnnotation
    public ResponseEntity<Page<PautaResponseDTO>> listar(@Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenar") @RequestParam(defaultValue = "titulo") String sortBy,
            @Parameter(description = "Direção da ordenação (asc ou desc)") @RequestParam(defaultValue = "asc") String direction) {
        Page<PautaResponseDTO> pautas = pautaService.listarPautas(page, size, sortBy, direction);
        return ResponseEntity.ok(pautas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pauta por ID")
    @GetSwaggerAnnotation
    public ResponseEntity<PautaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pautaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pauta por ID")
    @PutSwaggerAnnotation
    public ResponseEntity<PautaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PautaRequestDTO dto) {

        return ResponseEntity.ok(pautaService.atualizarPauta(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pauta por ID")
    @DeleteSwaggerAnnotation
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pautaService.deletarPauta(id);
        return ResponseEntity.noContent().build();
    }
}
