package com.obs.recruitment.order.item.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class OrderSeq {
    @Id
    private Integer id;

    @Column(name = "ORDER_ID_INC")
    private Integer orderIdInc;
}
