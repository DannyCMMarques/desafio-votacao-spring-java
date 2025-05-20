package com.crud.demo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.exceptions.sessao.SessaoJaFinalizadaException;
import com.crud.demo.exceptions.sessao.SessaoJaIniciadaException;
import com.crud.demo.exceptions.sessao.SessaoNaoIniciadaException;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sessao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pauta", nullable = false)
    private Pauta pauta;

    private Double duracao = 1.00;

    @Column(name = "horario_inicio")
    private LocalDateTime horarioInicio;

    @Column(name = "horario_fim")
    private LocalDateTime horarioFim;

    @Enumerated(EnumType.STRING)
    private StatusSessaoEnum status = StatusSessaoEnum.NAO_INICIADA;

    @OneToMany(mappedBy = "sessao")
    @JsonManagedReference
    private List<Voto> votos = new ArrayList<>();

    public void iniciarSessao(LocalDateTime horario) {
        if (this.status == StatusSessaoEnum.EM_ANDAMENTO) {
            throw new SessaoJaIniciadaException();
        } else if (this.status == StatusSessaoEnum.FINALIZADA) {
            throw new SessaoJaFinalizadaException();
        }
        this.status = StatusSessaoEnum.EM_ANDAMENTO;
        this.horarioInicio = horario;
    }

    public void finalizarSessao(LocalDateTime horario) {
        if (this.status == StatusSessaoEnum.NAO_INICIADA) {
            throw new SessaoNaoIniciadaException();
        } else if (this.status == StatusSessaoEnum.FINALIZADA) {
            throw new SessaoJaFinalizadaException();
        }
        this.status = StatusSessaoEnum.FINALIZADA;
        this.horarioFim = horario;
    }

}