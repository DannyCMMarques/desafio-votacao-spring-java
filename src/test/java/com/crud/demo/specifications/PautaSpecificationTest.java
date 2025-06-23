package com.crud.demo.specifications;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.jpa.domain.Specification;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.enums.StatusPautaEnum;

@ExtendWith(MockitoExtension.class)
class PautaSpecificationTest {

    @Mock
    private Root<Pauta> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private Predicate predicate;

    @Test
    @DisplayName("Deve retornar null quando titulo for null ou vazio em tituloContendo")
    void retornaNullParaTituloNuloOuVazio() {
        Specification<Pauta> specNull = PautaSpecification.tituloContendo(null);
        Specification<Pauta> specEmpty = PautaSpecification.tituloContendo("");
        Specification<Pauta> specSpaces = PautaSpecification.tituloContendo("   ");

        assertNull(specNull.toPredicate(root, query, cb));
        assertNull(specEmpty.toPredicate(root, query, cb));
        assertNull(specSpaces.toPredicate(root, query, cb));
    }

    @Test
    @DisplayName("Deve criar Predicate LIKE ignorando case em tituloContendo")
    void criaLikePredicateParaTituloValido() {
        String filtro = "Minha Pauta";
        Path<Object> tituloPath = mock(Path.class);
        when(root.get("titulo")).thenReturn(tituloPath);
        Expression<String> tituloExpression = (Expression<String>) (Path<?>) tituloPath;
        when(cb.lower(tituloExpression)).thenReturn(tituloExpression);
        when(cb.like(tituloExpression, "%" + filtro.toLowerCase() + "%")).thenReturn(predicate);

        Specification<Pauta> spec = PautaSpecification.tituloContendo(filtro);
        Predicate result = spec.toPredicate(root, query, cb);

        assertSame(predicate, result);
        verify(root).get("titulo");
        verify(cb).lower(tituloExpression);
        verify(cb).like(tituloExpression, "%" + filtro.toLowerCase() + "%");
    }

    @Test
    @DisplayName("Deve retornar null quando status for null em statusIgual")
    void retornaNullParaStatusNulo() {
        Specification<Pauta> spec = PautaSpecification.statusIgual(null);
        assertNull(spec.toPredicate(root, query, cb));
    }

    @Test
    @DisplayName("Deve criar Predicate EQUAL para statusIgual quando status não for nulo")
    void criaEqualPredicateParaStatusValido() {
        StatusPautaEnum status = StatusPautaEnum.EM_VOTACAO;
        Path<Object> statusPath = mock(Path.class);
        when(root.get("status")).thenReturn(statusPath);
        when(cb.equal(statusPath, status)).thenReturn(predicate);

        Specification<Pauta> spec = PautaSpecification.statusIgual(status);
        Predicate result = spec.toPredicate(root, query, cb);

        assertSame(predicate, result);
        verify(root).get("status");
        verify(cb).equal(statusPath, status);
    }
}
