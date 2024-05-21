package com.ourpos.domain.order;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.store.Store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

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
    public HallOrder(Customer customer, Store store, Boolean orderTakeoutYn, @Singular List<OrderDetail> orderDetails) {
        super(customer, store, orderDetails);
        this.status = HallStatus.WAITING;
        this.orderTakeoutYn = orderTakeoutYn;
    }

    @Override
    public void cancelOrder() {
        this.status = HallStatus.CANCELED;
    }

    @Override
    public void acceptOrder() {
        this.status = HallStatus.COOKING;
    }

    @Override
    public void startDeliveryOrder() {
    }

    @Override
    public void completeOrder() {
        this.status = HallStatus.COMPLETED;
    }
}
