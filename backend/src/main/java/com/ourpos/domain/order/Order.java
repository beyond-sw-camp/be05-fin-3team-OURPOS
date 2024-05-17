package com.ourpos.domain.order;

import java.time.LocalDateTime;

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
import jakarta.persistence.Table;

import com.ourpos.domain.BaseEntity;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.store.Store;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    protected Order(Customer customer, Store store, Integer price) {
        this.customer = customer;
        this.store = store;
        this.price = price;
    }
}
