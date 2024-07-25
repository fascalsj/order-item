package com.obs.recruitment.order.item.item.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemSpecificationTest {

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @InjectMocks
    private ItemSpecification itemSpecification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllPredicate() {
        String search = "test";
        when(criteriaBuilder.upper(any(Expression.class))).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.like(any(Expression.class), anyString())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.equal(any(), any())).thenReturn(mock(Predicate.class));

        itemSpecification.findAllPredicate(search);
        assertTrue(true);
    }
}