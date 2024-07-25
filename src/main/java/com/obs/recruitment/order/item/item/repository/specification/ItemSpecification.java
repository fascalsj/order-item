package com.obs.recruitment.order.item.item.repository.specification;

import com.obs.recruitment.order.item.item.model.Item;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemSpecification {
    public Specification<Item> findAllPredicate(String search) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" +
                    search.toUpperCase() + "%"
            ));
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        });
    }
}
