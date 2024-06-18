package com.ourpos.domain.storeorder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.ourpos.domain.BaseEntity;
import com.ourpos.domain.store.Store;

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
    @Enumerated(EnumType.STRING)
    private StoreOrderStatus status;

    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @OneToMany(mappedBy = "storeOrder", cascade = CascadeType.ALL)
    private List<StoreOrderDetail> storeOrderDetails = new ArrayList<>();

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Builder
    protected StoreOrder(Integer price, Integer quantity, Store store) {
        this.price = price;
        this.quantity = quantity;
        this.status = StoreOrderStatus.WAITING;
        this.store = store;
    }

    public void acceptedOrder() {
        if (this.status != StoreOrderStatus.WAITING) {
            throw new IllegalArgumentException("대기중인 주문만 승인 가능합니다.");
        }
        this.status = StoreOrderStatus.ACCEPTED;

    }

    public void deliveringOrder() {
        if (this.status != StoreOrderStatus.ACCEPTED) {
            throw new IllegalArgumentException("대기중인 주문만 배달 가능합니다.");
        }
        this.status = StoreOrderStatus.DELIVERING;
        this.deliveryDate = LocalDateTime.now(); 

    }

    public void completeOrder() {
        if (this.status != StoreOrderStatus.DELIVERING) {
            throw new IllegalArgumentException("배달중인 주문만 완료할 수 있습니다.");
        }
        this.status = StoreOrderStatus.COMPLETED;
    }

}
