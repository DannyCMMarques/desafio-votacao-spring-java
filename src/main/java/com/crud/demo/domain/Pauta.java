package com.crud.demo.domain;

import com.crud.demo.domain.enums.ResultadoPautaEnum;
import com.crud.demo.domain.enums.StatusPautaEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String titulo;

    @NotNull
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusPautaEnum status = StatusPautaEnum.NAO_VOTADA;

    @Column(name = "votos_contra", nullable = false)
    private Integer votosContra = 0;

    @Column(name = "votos_favor", nullable = false)
    private Integer votosFavor = 0;
    @Column(name = "votos_totais", nullable = false)

    private Integer votosTotais = 0;

    @Enumerated(EnumType.STRING)
    private ResultadoPautaEnum resultado = ResultadoPautaEnum.INDECISIVO;

    @PrePersist
    @PreUpdate
    private void calcularTotais() {
        this.votosTotais = (votosContra == null ? 0 : votosContra)
                + (votosFavor == null ? 0 : votosFavor);
    }
}
