package com.ourpos.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.ourpos.domain.BaseEntity;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.store.Store;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "order_type")
@Entity
@Table(name = "orders")
public abstract class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @JoinColumn(name = "customer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @Column(name = "order_price")
    private Integer price;

    @Column(name = "order_completed_datetime")
    private LocalDateTime completedDateTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    protected Order(Customer customer, Store store, @Singular List<OrderDetail> orderDetails) {
        this.customer = customer;
        this.store = store;
        for (OrderDetail orderDetail : orderDetails) {
            addOrderDetail(orderDetail);
        }
        this.price = calculatePrice();
    }

    // 연관관계 편의 메서드
    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }

    public Integer calculatePrice() {
        return orderDetails.stream()
            .mapToInt(OrderDetail::getPrice)
            .sum();
    }

    // 주문 완료 시간 설정
    public void setCompleteOrderTime(LocalDateTime completedDateTime) {
        this.completedDateTime = completedDateTime;
    }

    public void checkMinimumOrderPrice() {
        if (price < store.getMinimumOrderPrice()) {
            throw new IllegalArgumentException("최소 주문 금액을 충족하지 못했습니다.");
        }
    }
}
