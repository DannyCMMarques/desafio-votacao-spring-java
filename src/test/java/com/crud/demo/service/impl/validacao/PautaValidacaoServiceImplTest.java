package com.crud.demo.service.impl.validacao;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.enums.StatusPautaEnum;
import com.crud.demo.exceptions.pauta.PautaNaoCadastradaException;
import com.crud.demo.exceptions.pauta.PautaVotadaException;
import com.crud.demo.repositories.PautaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do PautaValidacaoServiceImpl")
class PautaValidacaoServiceImplTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaValidacaoServiceImpl service;

    @Test
    @DisplayName("Deve retornar pauta quando ainda não votada")
    void deveRetornarQuandoNaoVotada() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setStatus(StatusPautaEnum.NAO_VOTADA);

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(pautaRepository.findAllByStatus(StatusPautaEnum.NAO_VOTADA))
                .thenReturn(List.of(pauta));

        Pauta resultado = service.verificarStatusNaoVotada(1L);

        assertThat(resultado).isEqualTo(pauta);
        verify(pautaRepository).findAllByStatus(StatusPautaEnum.NAO_VOTADA);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pauta já foi votada")
    void deveLancarQuandoVotada() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setStatus(StatusPautaEnum.VOTADA);

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(pautaRepository.findAllByStatus(StatusPautaEnum.NAO_VOTADA))
                .thenReturn(List.of()); // não contém a pauta

        assertThrows(PautaVotadaException.class,
                () -> service.verificarStatusNaoVotada(1L));
    }

    @Test
    @DisplayName("Deve retornar pauta quando existir")
    void deveRetornarQuandoExistir() {
        Pauta pauta = new Pauta();
        pauta.setId(2L);

        when(pautaRepository.findById(2L)).thenReturn(Optional.of(pauta));

        Pauta resultado = service.validarEObterPauta(2L);

        assertThat(resultado).isEqualTo(pauta);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pauta não existir")
    void deveLancarQuandoNaoExistir() {
        when(pautaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(PautaNaoCadastradaException.class,
                () -> service.validarEObterPauta(2L));
    }

}
