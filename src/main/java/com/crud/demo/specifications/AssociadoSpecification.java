package com.crud.demo.specifications;
import org.springframework.data.jpa.domain.Specification;

import com.crud.demo.domain.Associado;

public class AssociadoSpecification {

    public static Specification<Associado> cpfContem(String cpf) {
        return (root, query, criteriaBuilder) ->
            cpf == null || cpf.trim().isEmpty()
                ? null
                : criteriaBuilder.like(root.get("cpf"), "%" + cpf + "%");
    }
}
