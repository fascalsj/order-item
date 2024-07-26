package com.obs.recruitment.order.item.order.repository;

import com.obs.recruitment.order.item.order.model.OrderSeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface OrderSeqRepository extends JpaRepository<OrderSeq, Integer>, JpaSpecificationExecutor {
}
