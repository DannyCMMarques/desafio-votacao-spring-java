package com.crud.demo.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.repositories.SessaoRepository;
import com.crud.demo.service.EncerraVotacaoService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do ScheduleVerificacaoServiceImpl")
class ScheduleVerificacaoServiceImplTest {

    @Mock
     private SessaoRepository sessaoRepository;
    @Mock
     private EncerraVotacaoService encerraVotacaoService;

    @InjectMocks 
    private ScheduleVerificacaoServiceImpl scheduleService;

    @Test
    @DisplayName("Deve encerrar todas as sessões vencidas")
    void deveEncerrarSessoesVencidas() {
        ZoneId zone = ZoneId.of("America/Sao_Paulo");
        LocalDateTime agora = LocalDateTime.of(2025, 5, 18, 20, 0);

        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(() -> LocalDateTime.now(zone)).thenReturn(agora);

            List<Long> idsVencidos = List.of(1L, 2L, 3L);
            when(sessaoRepository.findIdsVencidas(agora)).thenReturn(idsVencidos);

            scheduleService.verificarDuracao();

            verify(sessaoRepository).findIdsVencidas(agora);
            idsVencidos.forEach(id -> verify(encerraVotacaoService).finalizarSessao(id));
        }
    }

    @Test
    @DisplayName("Não deve chamar serviço de encerramento quando não há sessões vencidas")
    void naoDeveEncerrarQuandoNaoHaVencidas() {
        ZoneId zone = ZoneId.of("America/Sao_Paulo");
        LocalDateTime agora = LocalDateTime.of(2025, 5, 18, 21, 0);

        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(() -> LocalDateTime.now(zone)).thenReturn(agora);

            when(sessaoRepository.findIdsVencidas(agora)).thenReturn(List.of());

            scheduleService.verificarDuracao();

            verify(sessaoRepository).findIdsVencidas(agora);
            verifyNoInteractions(encerraVotacaoService);
}
    }

}