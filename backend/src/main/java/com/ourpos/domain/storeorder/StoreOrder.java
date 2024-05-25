package com.ourpos.domain.storeorder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.ourpos.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store_orders")
public class StoreOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_order_id")
    private Long id;

    @Column(name = "store_order_price")
    private Integer price;

    @Column(name = "store_order_quantity")
    private Integer quantity;

    @Column(name = "store_order_status")
    private StoreOrderStatus status;

    @Builder
    public StoreOrder(Integer price, Integer quantity) {
        this.price = price;
        this.quantity = quantity;

    }
}
