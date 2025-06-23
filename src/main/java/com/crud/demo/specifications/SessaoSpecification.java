package com.crud.demo.specifications;


import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.StatusSessaoEnum;
import org.springframework.data.jpa.domain.Specification;

public class SessaoSpecification {

    public static Specification<Sessao> comPautaId(Long pautaId) {
        return (root, query, cb) -> pautaId == null
            ? null
            : cb.equal(root.get("pauta").get("id"), pautaId);
    }

    public static Specification<Sessao> comStatus(StatusSessaoEnum status) {
        return (root, query, cb) -> status == null
            ? null
            : cb.equal(root.get("status"), status);
    }
}
