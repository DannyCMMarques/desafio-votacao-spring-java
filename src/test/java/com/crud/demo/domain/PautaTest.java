package com.crud.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.crud.demo.domain.enums.ResultadoPautaEnum;

@DisplayName("Pauta – regras de domínio internas")
class PautaTest {

        private void invocarCalcularTotais(Pauta pauta) throws Exception {
            Method m = Pauta.class.getDeclaredMethod("calcularTotais");
            m.setAccessible(true);
            m.invoke(pauta);
        }

        @Test
        @DisplayName("Deve somar votosFavor e votosContra (valores não-nulos)")
        void deveSomarQuandoValoresPresentes() throws Exception {
            Pauta pauta = new Pauta();
            pauta.setVotosFavor(5L);
            pauta.setVotosContra(3L);

            invocarCalcularTotais(pauta);

            assertThat(pauta.getVotosTotais()).isEqualTo(8L);
        }

        @Test
        @DisplayName("Deve tratar null como 0 ao calcular votosTotais")
        void deveTratarNullComoZero() throws Exception {
            Pauta pauta = new Pauta();
            pauta.setVotosFavor(null); 
            pauta.setVotosContra(7L);

            invocarCalcularTotais(pauta);

            assertThat(pauta.getVotosTotais()).isEqualTo(7L);

            pauta.setVotosFavor(4L);
            pauta.setVotosContra(null);

            invocarCalcularTotais(pauta);

            assertThat(pauta.getVotosTotais()).isEqualTo(4L);
        }
    

        @Test
        @DisplayName("Deve marcar APROVADO quando votosFavor > votosContra")
        void deveMarcarAprovado() {
            Pauta pauta = new Pauta();
            pauta.setVotosFavor(10L);
            pauta.setVotosContra(4L);

            pauta.determinarResultado();

            assertThat(pauta.getResultado()).isEqualTo(ResultadoPautaEnum.APROVADO);
        }

        @Test
        @DisplayName("Deve marcar REPROVADO quando votosContra > votosFavor")
        void deveMarcarReprovado() {
            Pauta pauta = new Pauta();
            pauta.setVotosFavor(2L);
            pauta.setVotosContra(5L);

            pauta.determinarResultado();

            assertThat(pauta.getResultado()).isEqualTo(ResultadoPautaEnum.REPROVADO);
        }

        @Test
        @DisplayName("Deve marcar INDECISIVO quando quantidades são iguais")
        void deveMarcarIndecisivo() {
            Pauta pauta = new Pauta();
            pauta.setVotosFavor(6L);
            pauta.setVotosContra(6L);

            pauta.determinarResultado();

            assertThat(pauta.getResultado()).isEqualTo(ResultadoPautaEnum.INDECISIVO);
        }
    
}
