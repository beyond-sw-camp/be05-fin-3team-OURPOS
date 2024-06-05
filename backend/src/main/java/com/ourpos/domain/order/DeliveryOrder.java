package com.ourpos.domain.order;

import java.time.LocalDateTime;
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

    public void cancelOrder() {
        if (this.status != DeliveryStatus.WAITING) {
            throw new IllegalArgumentException("대기중인 주문만 취소할 수 있습니다.");
        }
        this.status = DeliveryStatus.CANCELED;
    }

    public void acceptOrder() {
        if (this.status != DeliveryStatus.WAITING) {
            throw new IllegalArgumentException("대기중인 주문만 접수할 수 있습니다.");
        }
        this.status = DeliveryStatus.COOKING;
    }

    public void startDelivery() {
        if (this.status != DeliveryStatus.COOKING) {
            throw new IllegalArgumentException("조리중인 주문만 배달을 시작할 수 있습니다.");
        }
        this.status = DeliveryStatus.DELIVERING;
    }

    public void completeOrder(LocalDateTime completeOrderTime) {
        if (this.status != DeliveryStatus.DELIVERING) {
            throw new IllegalArgumentException("배달중인 주문만 완료할 수 있습니다.");
        }
        this.status = DeliveryStatus.COMPLETED;
        super.setCompleteOrderTime(completeOrderTime);
    }

    // 라이더 배정
    public void assignRider(Rider rider) {
        if (this.status != DeliveryStatus.WAITING && this.status != DeliveryStatus.COOKING) {
            throw new IllegalArgumentException("대기중 또는 조리중인 주문만 라이더를 배정할 수 있습니다.");
        }
        this.rider = rider;
    }

    // 배달 예상 시간 설정
    public void setEstimatedTime(LocalTime estimatedTime) {
        if (this.status != DeliveryStatus.WAITING && this.status != DeliveryStatus.COOKING) {
            throw new IllegalArgumentException("대기중 또는 조리중인 주문만 배달 예상 시간을 설정할 수 있습니다.");
        }
        this.estimatedTime = estimatedTime;
    }
}
