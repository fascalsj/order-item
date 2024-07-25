package com.obs.recruitment.order.item.item.repository;

import com.obs.recruitment.order.item.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ItemRepository extends JpaRepository<Item, Integer>, JpaSpecificationExecutor {
    Optional<Item> findByIdAndIsDeleted(Integer id, boolean deleted);
}
