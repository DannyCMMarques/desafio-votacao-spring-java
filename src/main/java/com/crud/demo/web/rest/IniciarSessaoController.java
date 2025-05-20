package com.crud.demo.web.rest;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.service.IniciarSessaoService;
import com.crud.demo.service.dto.sessao.SessaoIniciadaResponseDTO;
import com.crud.demo.service.dto.sessao.SessaoResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "IniciarSessao", description = "Endpoints para iniciar sessão de votação")
@RequestMapping("/api/v1/sessao")
public class IniciarSessaoController {

    private final IniciarSessaoService iniciarSessao;

    @PatchMapping("/{id}/start")

    @Operation(summary = "Iniciar uma sessão existente", description = "Insira o ID da sessão que deseja iniciar.")
    public ResponseEntity<SessaoResponseDTO> iniciarSessao(
            @Parameter(description = "ID da sessão que vai ser iniciada") @PathVariable("id") Long idSessao) {
        SessaoIniciadaResponseDTO sessaoIniciadaResponseDTO = iniciarSessao.executar(idSessao);
        URI location = URI.create("/api/v1/votacao/" + idSessao + "/start");
        return ResponseEntity.created(location).body(sessaoIniciadaResponseDTO);
    }
}
