package com.crud.demo.web.rest;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.service.VotacaoService;
import com.crud.demo.service.dto.sessao.SessaoIniciadaResponseDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;
import com.crud.demo.web.rest.utils.annotations.PostSwaggerAnnotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Votação", description = "Endpoints para iniciar sessão de votação")
@RequestMapping("/api/v1/votacao")
public class VotacaoController {

    private final VotacaoService votacaoService;

    @PatchMapping("/{id}/start")

    @Operation(summary = "Iniciar uma sessão de votação existente", description = "Insira o ID da sessão que deseja iniciar.")
    public ResponseEntity<SessaoResponseDTO> iniciarSessao(
            @PathVariable("id") Long idSessao) {
        SessaoIniciadaResponseDTO sessaoIniciadaResponseDTO = votacaoService.iniciarVotacao(idSessao);
        URI location = URI.create("/api/v1/votacao/" + idSessao + "/start");
        return ResponseEntity.created(location).body(sessaoIniciadaResponseDTO);
    }
}
