package com.ourpos.domain.order;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.rider.Rider;
import com.ourpos.domain.store.Store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("delivery")
@Entity
@Table(name = "delivery_order")
public class DeliveryOrder extends Order {

    @JoinColumn(name = "order_address_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderAddress orderAddress;

    @JoinColumn(name = "rider_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Rider rider;

    @Column(name = "delivery_order_owner_message")
    private String ownerMessage;

    @Column(name = "delivery_order_rider_message")
    private String riderMessage;

    @Column(name = "delivery_order_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Column(name = "delivery_order_tip")
    private Integer tip;

    @Column(name = "delivery_order_disposable_yn")
    private Boolean disposableYn;

    @Column(name = "delivery_order_estimated_time")
    private LocalTime estimatedTime;

    @Builder
    private DeliveryOrder(Customer customer, Store store, OrderAddress orderAddress, String ownerMessage,
        String riderMessage, Integer tip, Boolean disposableYn, @Singular List<OrderDetail> orderDetails) {
        super(customer, store, orderDetails);
        this.orderAddress = orderAddress;
        this.ownerMessage = ownerMessage;
        this.riderMessage = riderMessage;
        this.tip = tip;
        this.disposableYn = disposableYn;
        this.status = DeliveryStatus.WAITING;
    }

    @Override
    public void cancelOrder() {
        this.status = DeliveryStatus.CANCELED;
    }

    @Override
    public void acceptOrder() {
        this.status = DeliveryStatus.COOKING;
    }

    @Override
    public void startDeliveryOrder() {
        this.status = DeliveryStatus.DELIVERING;
    }

    @Override
    public void completeOrder() {
        this.status = DeliveryStatus.COMPLETED;
    }
}
