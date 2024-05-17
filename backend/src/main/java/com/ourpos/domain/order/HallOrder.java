package com.ourpos.domain.order;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.store.Store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("hall")
@Entity
@Table(name = "hall_order")
public class HallOrder extends Order {

    @Column(name = "hall_order_status")
    @Enumerated(EnumType.STRING)
    private HallStatus status;

    @Column(name = "hall_order_takeout_yn")
    private Boolean orderTakeoutYn;

    @Builder
    public HallOrder(Customer customer, Store store, Integer price, Boolean orderTakeoutYn) {
        super(customer, store, price);
        this.status = HallStatus.WAITING;
        this.orderTakeoutYn = orderTakeoutYn;
    }
}
