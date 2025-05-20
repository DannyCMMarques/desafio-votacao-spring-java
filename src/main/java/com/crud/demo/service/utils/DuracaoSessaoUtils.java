package com.crud.demo.service.utils;

import com.crud.demo.domain.enums.DuracaoSessaoEnum;

public final class DuracaoSessaoUtils {
    public static Double converterMinutos(Double valor, DuracaoSessaoEnum unidade) {
        switch (unidade) {
            case MIN:
                return valor;
            case H:
                return valor * 60;
            case SEG:
                return valor / 60;
            default:
                return valor;

        }
    }
}