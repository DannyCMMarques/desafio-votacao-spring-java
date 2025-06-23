package com.crud.demo.specifications;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.jpa.domain.Specification;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.domain.Sessao;
import com.crud.demo.domain.enums.StatusSessaoEnum;

@ExtendWith(MockitoExtension.class)
class SessaoSpecificationTest {

    @Mock
    private Root<Sessao> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private Predicate predicate;

    @Test
    @DisplayName("Deve retornar null quando pautaId for null em comPautaId")
    void retornaNullParaPautaIdNulo() {
        Specification<Sessao> spec = SessaoSpecification.comPautaId(null);
        assertNull(spec.toPredicate(root, query, cb));
    }

    @Test
    @DisplayName("Deve criar Predicate EQUAL para comPautaId quando pautaId não for null")
    void criaEqualParaPautaIdValido() {
        Long pautaId = 42L;

        @SuppressWarnings("unchecked")
        Path<Object> pautaPath = mock(Path.class);
        @SuppressWarnings("unchecked")
        Path<Object> idPath = mock(Path.class);

        when(root.get("pauta")).thenReturn(pautaPath);
        when(pautaPath.get("id")).thenReturn(idPath);
        when(cb.equal(idPath, pautaId)).thenReturn(predicate);

        Specification<Sessao> spec = SessaoSpecification.comPautaId(pautaId);
        Predicate result = spec.toPredicate(root, query, cb);

        assertSame(predicate, result);
        verify(root, times(1)).get("pauta");
        verify(pautaPath, times(1)).get("id");
        verify(cb, times(1)).equal(idPath, pautaId);
    }

    @Test
    @DisplayName("Deve retornar null quando status for null em comStatus")
    void retornaNullParaStatusNulo() {
        Specification<Sessao> spec = SessaoSpecification.comStatus(null);
        assertNull(spec.toPredicate(root, query, cb));
    }

    @Test
    @DisplayName("Deve criar Predicate EQUAL para comStatus quando status não for null")
    void criaEqualParaStatusValido() {
        StatusSessaoEnum status = StatusSessaoEnum.EM_ANDAMENTO;
        @SuppressWarnings("unchecked")
        Path<Object> statusPath = mock(Path.class);
        when(root.get("status")).thenReturn(statusPath);
        when(cb.equal(statusPath, status)).thenReturn(predicate);

        Specification<Sessao> spec = SessaoSpecification.comStatus(status);
        Predicate result = spec.toPredicate(root, query, cb);

        assertSame(predicate, result);
        verify(root).get("status");
        verify(cb).equal(statusPath, status);
    }
}
