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

import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.service.SessaoService;
import com.crud.demo.service.dto.sessao.SessaoRequestDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;
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
@RequestMapping("/api/v1/sessao")
@RequiredArgsConstructor
@Tag(name = "Sessão", description = "Endpoints para operações com sessões de votação")
public class SessaoController {
    private final SessaoService sessaoService;

    @PostMapping
    @Operation(summary = "Cadastrar uma nova sessão de votação")
    @PostSwaggerAnnotation
    public ResponseEntity<SessaoResponseDTO> cadastrarSessao(
            @Valid @RequestBody SessaoRequestDTO sessaoRequestDTO) {

        SessaoResponseDTO sessaoCriada = sessaoService.criarSessao(sessaoRequestDTO);
        URI location = UriLocationUtils.criarLocationUri("/api/v1/sessao", sessaoCriada.getId());
        return ResponseEntity.created(location).body(sessaoCriada);
    }

    @GetMapping
    @Operation(summary = "Listar sessões com filtros opcionais")
    @GetSwaggerAnnotation
    public ResponseEntity<Page<SessaoResponseDTO>> listarSessoes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) Long pautaId,
            @RequestParam(required = false) StatusSessaoEnum status) {
        Page<SessaoResponseDTO> sessoes = sessaoService.listarSessoesComFiltro(page, size, sortBy, direction, pautaId, status);
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sessão por ID")
    @GetSwaggerAnnotation
    public ResponseEntity<SessaoResponseDTO> buscarPorId(@PathVariable Long id) {
        SessaoResponseDTO sessao = sessaoService.buscarPorId(id);
        return ResponseEntity.ok(sessao);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sessão por ID")
    @PutSwaggerAnnotation
    public ResponseEntity<SessaoResponseDTO> atualizarSessao(
            @PathVariable Long id,
            @Valid @RequestBody SessaoRequestDTO dto) {

        SessaoResponseDTO sessaoAtualizada = sessaoService.atualizarSessao(id, dto);
        return ResponseEntity.ok(sessaoAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar sessão por ID")
    @DeleteSwaggerAnnotation
    public ResponseEntity<Void> deletarSessao(@PathVariable Long id) {
        sessaoService.deletarSessao(id);
        return ResponseEntity.noContent().build();
    }
}
