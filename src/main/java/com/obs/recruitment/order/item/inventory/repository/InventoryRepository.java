package com.obs.recruitment.order.item.inventory.repository;

import com.obs.recruitment.order.item.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface InventoryRepository extends JpaRepository<Inventory, Integer>, JpaSpecificationExecutor {
    Optional<Inventory> findByIdAndIsDeleted(Integer id, boolean deleted);
}
