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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
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
    private Long votosContra = 0L;

    @Column(name = "votos_favor", nullable = false)
    private Long votosFavor = 0L;

    @Column(name = "votos_totais", nullable = false)

    private Long votosTotais = 0L;

    @Enumerated(EnumType.STRING)
    private ResultadoPautaEnum resultado = ResultadoPautaEnum.EM_ANDAMENTO;

    @PrePersist
    @PreUpdate
    private void calcularTotais() {
        this.votosTotais = (votosContra == null ? 0 : votosContra)
                + (votosFavor == null ? 0 : votosFavor);
    }

    public void iniciarVotacaoPauta() {
        this.status = StatusPautaEnum.EM_VOTACAO;
    }

    public void determinarResultado() {
        Long diferençasVoto = votosFavor - votosContra;
        this.resultado = diferençasVoto > 0 ? ResultadoPautaEnum.APROVADO
                : diferençasVoto < 0 ? ResultadoPautaEnum.REPROVADO : ResultadoPautaEnum.INDECISIVO;
    }

    public void finalizarVotacaoPauta() {
        this.status = StatusPautaEnum.VOTADA;
        log.info("PAUTA VOTADA ");
        this.determinarResultado();
    }

}
