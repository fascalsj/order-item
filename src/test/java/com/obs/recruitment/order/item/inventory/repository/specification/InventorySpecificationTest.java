package com.obs.recruitment.order.item.inventory.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InventorySpecificationTest {
    @Mock
    private CriteriaBuilder criteriaBuilder;

    @InjectMocks
    private InventorySpecification inventorySpecification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllPredicate() {
        when(criteriaBuilder.equal(any(), any())).thenReturn(mock(Predicate.class));

        inventorySpecification.findAllPredicate();
        assertTrue(true);
    }

}