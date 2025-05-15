package com.crud.demo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Sessao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pauta", nullable = false)
    private Pauta pauta;

    private Integer duracao = 1;

    @Column(name = "horario_inicio")
    private LocalDateTime horarioInicio;

    @Column(name = "horario_fim")
    private LocalDateTime horarioFim;

    @Enumerated(EnumType.STRING)
    private StatusSessaoEnum status = StatusSessaoEnum.NAO_INICIADA;

    @OneToMany(mappedBy = "sessao")
    @JsonManagedReference
    private List<Voto> votos = new ArrayList<>();
}
