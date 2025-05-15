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

import com.crud.demo.service.AssociadoService;
import com.crud.demo.service.dto.associado.AssociadoRequestDTO;
import com.crud.demo.service.dto.associado.AssociadoResponseDTO;
import com.crud.demo.web.rest.utils.UriLocationUtils;
import com.crud.demo.web.rest.utils.annotations.DeleteSwaggerAnnotation;
import com.crud.demo.web.rest.utils.annotations.GetSwaggerAnnotation;
import com.crud.demo.web.rest.utils.annotations.PostSwaggerAnnotation;
import com.crud.demo.web.rest.utils.annotations.PutSwaggerAnnotation;

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
    public ResponseEntity<AssociadoResponseDTO> criarAssociado(
            @Valid @RequestBody AssociadoRequestDTO associadoRequestDTO) {
        AssociadoResponseDTO associadoCriado = associadoService.cadastrarAssociado(associadoRequestDTO);
        URI location = UriLocationUtils.criarLocationUri("api/v1/associado", associadoCriado.getId());
        return ResponseEntity.created(location).body(associadoCriado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar associado por ID")
    @GetSwaggerAnnotation
    public ResponseEntity<AssociadoResponseDTO> buscarPorId(@PathVariable Long id) {
        AssociadoResponseDTO associadoEncontrado = associadoService.buscarAssociadoPorId(id);
        return ResponseEntity.ok(associadoEncontrado);
    }

    @GetMapping
    @Operation(summary = "Listar associados com paginação")
    @GetSwaggerAnnotation
    public ResponseEntity<Page<AssociadoResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<AssociadoResponseDTO> associadosRetornados = associadoService.listarTodosAssociados(page, size, sortBy,
                direction);
        return ResponseEntity.ok(associadosRetornados);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um associado")
    @PutSwaggerAnnotation
    public ResponseEntity<AssociadoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AssociadoRequestDTO requestDTO) {
        AssociadoResponseDTO associadoAtualizadoResponse = associadoService.atualizarAssociado(id, requestDTO);
        return ResponseEntity.ok(associadoAtualizadoResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um associado")
    @DeleteSwaggerAnnotation
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        associadoService.deletarAssociado(id);
        return ResponseEntity.noContent().build();
    }
}
