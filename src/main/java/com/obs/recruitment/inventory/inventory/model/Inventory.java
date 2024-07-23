package com.obs.recruitment.inventory.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ITEM_ID")
    private Integer itemId;

    @Column(name = "QTY")
    private Integer qty;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;
}
