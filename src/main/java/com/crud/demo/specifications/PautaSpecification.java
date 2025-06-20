package com.crud.demo.specifications;
import org.springframework.data.jpa.domain.Specification;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.enums.StatusPautaEnum;

public class PautaSpecification {

    public static Specification<Pauta> tituloContendo(String titulo) {
        return (root, query, cb) -> {
            if (titulo == null || titulo.trim().isEmpty()) return null;
            return cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%");
        };
    }

    public static Specification<Pauta> statusIgual(StatusPautaEnum status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }
}
