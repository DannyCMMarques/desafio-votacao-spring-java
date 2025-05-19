package com.crud.demo.service.impl.validacao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.domain.Associado;
import com.crud.demo.exceptions.associado.AssociadoNaoEncontradoException;
import com.crud.demo.exceptions.associado.CPFJaCadastradoException;
import com.crud.demo.repositories.AssociadoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do AssociadoValidacaoServiceImpl")
class AssociadoValidacaoServiceImplTest {

    @Mock
    private AssociadoRepository associadoRepository;

    @InjectMocks
    private AssociadoValidacaoServiceImpl service;

    private String CPF = "11111111111";
    private String CPF2 = "22222222222";

    @Test
    @DisplayName("Deve lançar exceção quando CPF já existir")
    void deveLancarExcecaoQuandoCpfJaCadastrado() {
        when(associadoRepository.findByCpf(CPF))
                .thenReturn(Optional.of(new Associado()));

        assertThrows(CPFJaCadastradoException.class,
                () -> service.validarCPFCadastro(CPF));
    }

    @Test
    @DisplayName("Não deve lançar exceção quando CPF for novo")
    void naoDeveLancarQuandoCpfNovo() {
        when(associadoRepository.findByCpf(CPF))
                .thenReturn(Optional.empty());

        service.validarCPFCadastro(CPF);

        verify(associadoRepository).findByCpf(CPF);
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF pertencer a outro associado")
    void deveLancarQuandoCpfDeOutroAssociado() {
        Associado outro = new Associado();
        outro.setId(2L);

        when(associadoRepository.findByCpf(CPF2))
                .thenReturn(Optional.of(outro));

        assertThrows(CPFJaCadastradoException.class,
                () -> service.validarCPFAtualizacao(CPF2, 1L));
    }

    @Test
    @DisplayName("Não deve lançar exceção quando CPF pertencer ao mesmo associado")
    void naoDeveLancarQuandoMesmoAssociado() {
        Associado mesmo = new Associado();
        mesmo.setId(1L);

        when(associadoRepository.findByCpf(CPF2))
                .thenReturn(Optional.of(mesmo));

        service.validarCPFAtualizacao(CPF2, 1L);

        verify(associadoRepository).findByCpf(CPF2);
    }

    @Test
    @DisplayName("Não deve lançar exceção quando CPF não existir")
    void naoDeveLancarQuandoCpfNaoExiste() {
        when(associadoRepository.findByCpf(CPF2))
                .thenReturn(Optional.empty());

        service.validarCPFAtualizacao(CPF2, 1L);

        verify(associadoRepository).findByCpf(CPF2);
    }

    @Test
    @DisplayName("Deve retornar associado quando existir")
    void deveRetornarAssociadoQuandoExistir() {
        Associado associado = new Associado();
        associado.setId(5L);

        when(associadoRepository.findById(5L))
                .thenReturn(Optional.of(associado));

        Associado resultado = service.validarExistencia(5L);

        assertThat(resultado).isEqualTo(associado);
    }

    @Test
    @DisplayName("Deve lançar exceção quando associado não existir")
    void deveLancarExcecaoQuandoNaoExistir() {
        when(associadoRepository.findById(5L))
                .thenReturn(Optional.empty());

        assertThrows(AssociadoNaoEncontradoException.class,
                () -> service.validarExistencia(5L));

    }
}
