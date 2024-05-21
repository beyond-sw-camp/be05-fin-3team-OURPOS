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

    public void cancelOrder() {
        if (this.status == HallStatus.COMPLETED) {
            throw new IllegalArgumentException("이미 완료된 주문은 취소할 수 없습니다.");
        }
        this.status = HallStatus.CANCELED;
    }

    public void acceptOrder() {
        if (this.status != HallStatus.WAITING) {
            throw new IllegalArgumentException("대기중인 주문만 접수할 수 있습니다.");
        }
        this.status = HallStatus.COOKING;
    }

    public void completeOrder() {
        if (this.status != HallStatus.COOKING) {
            throw new IllegalArgumentException("조리중인 주문만 완료할 수 있습니다.");
        }
        this.status = HallStatus.COMPLETED;
    }
}
