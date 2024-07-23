package com.obs.recruitment.inventory.inventory.repository.specification;

import com.obs.recruitment.inventory.inventory.model.Inventory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventorySpecification {
    public Specification<Inventory> findAllPredicate() {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        });
    }
}
