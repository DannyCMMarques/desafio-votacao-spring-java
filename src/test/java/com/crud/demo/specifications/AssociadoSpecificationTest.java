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

import com.crud.demo.domain.Associado;

@ExtendWith(MockitoExtension.class)
class AssociadoSpecificationTest {

    @Mock
    private Root<Associado> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private Path<Object> cpfPath;

    @Mock
    private Predicate likePredicate;

    @Test
    @DisplayName("Deve retornar null quando o CPF fornecido for null")
    void retornaNullParaCpfNulo() {
        Specification<Associado> spec = AssociadoSpecification.cpfContem(null);
        Predicate result = spec.toPredicate(root, query, cb);
        assertNull(result);
    }

    @Test
    @DisplayName("Deve retornar null quando o CPF fornecido for vazio ou espaços")
    void retornaNullParaCpfVazio() {
        Specification<Associado> specEmpty = AssociadoSpecification.cpfContem("");
        Specification<Associado> specSpaces = AssociadoSpecification.cpfContem("   ");

        assertNull(specEmpty.toPredicate(root, query, cb));
        assertNull(specSpaces.toPredicate(root, query, cb));
    }
@Test
@DisplayName("Deve criar Predicate LIKE ao filtrar CPF não vazio")
void criaLikePredicateParaCpfValido() {
    String filtro = "123456";

    when(root.get("cpf")).thenReturn(cpfPath);

    @SuppressWarnings("unchecked")
    Expression<String> cpfExpression = (Expression<String>)(Path<?>) cpfPath;

    when(cb.like(cpfExpression, "%" + filtro + "%")).thenReturn(likePredicate);

    Specification<Associado> spec = AssociadoSpecification.cpfContem(filtro);
    Predicate result = spec.toPredicate(root, query, cb);

    assertSame(likePredicate, result);
    verify(root).get("cpf");
    verify(cb).like(cpfExpression, "%" + filtro + "%");
}
}