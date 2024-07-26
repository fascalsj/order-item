package com.obs.recruitment.order.item.order.model;

import com.obs.recruitment.order.item.item.model.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Order {
    @Id
    @Column(name = "ORDER_NO")
    private String orderNo;

    @Column(name = "ITEM_ID")
    private Integer itemId;

    @Column(name = "QTY")
    private Integer qty;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Item item;
}
