package com.obs.recruitment.order.item.item.model;

import com.obs.recruitment.order.item.inventory.model.Inventory;
import com.obs.recruitment.order.item.order.model.Orders;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Setter
@Getter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "STOCK")
    private Integer stock;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @OneToMany(mappedBy = "item", targetEntity = Inventory.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Inventory> inventories;

    @OneToMany(mappedBy = "item", targetEntity = Inventory.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Orders> orders;
}
