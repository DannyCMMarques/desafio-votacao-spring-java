package com.crud.demo.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import com.crud.demo.repositories.PautaRepository;
import com.crud.demo.repositories.SessaoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do EncerraVotacaoServiceImpl")
class EncerraVotacaoServiceImplTest {

    @Mock
    private SessaoRepository sessaoRepository;
    @Mock
    private PautaRepository pautaRepository;
    @InjectMocks
   private EncerraVotacaoServiceImpl encerraVotacaoService;

    @Test
    @DisplayName("Deve encerrar a sessão e a pauta com sucesso")
    void deveEncerrarSessaoComSucesso() {
        Pauta pauta = spy(new Pauta());

        Sessao sessao = spy(new Sessao());
        sessao.setId(1L);
        sessao.setPauta(pauta);

        sessao.setStatus(StatusSessaoEnum.EM_ANDAMENTO);

        when(sessaoRepository.findById(1L)).thenReturn(Optional.of(sessao));

        encerraVotacaoService.finalizarSessao(1L);

        verify(sessaoRepository).findById(1L);
        verify(sessao).finalizarSessao(any(LocalDateTime.class));
        verify(pauta).finalizarVotacaoPauta();
        verify(sessaoRepository).save(sessao);
        verify(pautaRepository).save(pauta);
    }
}
