package com.ourpos.domain.store;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import com.ourpos.domain.BaseEntity;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.manager.Manager;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @JoinColumn(name = "manager_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Manager manager;

    @JoinColumn(name = "store_address_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private StoreAddress address;

    @JoinColumn(name = "kiosk_customer_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Customer kioskCustomer;

    @Column(name = "store_name")
    private String name;

    @Column(name = "store_phone")
    private String phone;

    @Column(name = "store_open_time")
    private LocalTime openTime;

    @Column(name = "store_close_time")
    private LocalTime closeTime;

    @Column(name = "store_minimum_order_price")
    private Integer minimumOrderPrice;

    @Column(name = "store_picture_url")
    private String pictureUrl;

    @Column(name = "store_close_yn")
    private Boolean closeYn;

    @Column(name = "store_closed_datetime")
    private LocalDateTime closedDateTime;

    @Builder
    private Store(Manager manager, StoreAddress address, Customer kioskCustomer, String name, String phone,
        LocalTime openTime, LocalTime closeTime, Integer minimumOrderPrice, String pictureUrl) {
        this.manager = manager;
        this.address = address;
        this.kioskCustomer = kioskCustomer;
        this.name = name;
        this.phone = phone;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minimumOrderPrice = minimumOrderPrice;
        this.pictureUrl = pictureUrl;
        this.closeYn = false;
    }

    public void closeStore() {
        this.closeYn = true;
        this.closedDateTime = LocalDateTime.now();
    }
}
