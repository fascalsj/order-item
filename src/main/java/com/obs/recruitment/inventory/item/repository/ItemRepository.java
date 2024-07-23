package com.obs.recruitment.inventory.item.repository;

import com.obs.recruitment.inventory.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ItemRepository extends JpaRepository<Item, Integer>, JpaSpecificationExecutor {
    Optional<Item> findByIdAndIsDeleted(Integer id, boolean b);
}
