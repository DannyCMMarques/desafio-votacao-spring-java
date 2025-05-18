/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.crud.demo.service.validacoes;

import com.crud.demo.domain.Pauta;

/**
 *
 * @author danielly.marques
 */
public interface PautaValidacaoService {

    Pauta verificarStatusNaoVotada(Long id);

    Pauta validarEObterPauta(Long id);

}
