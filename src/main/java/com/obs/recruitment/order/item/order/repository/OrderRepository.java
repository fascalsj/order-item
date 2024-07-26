package com.obs.recruitment.order.item.order.repository;

import com.obs.recruitment.order.item.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor {
    Optional<Order> findByOrderNoAndIsDeleted(String orderId, boolean deleted);
}
