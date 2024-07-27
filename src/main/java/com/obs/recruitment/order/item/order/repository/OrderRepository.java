package com.obs.recruitment.order.item.order.repository;

import com.obs.recruitment.order.item.order.model.Orders;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface OrderRepository extends CrudRepository<Orders, String>, JpaSpecificationExecutor {
    Optional<Orders> findByOrderNoAndIsDeleted(String orderId, boolean deleted);
}
