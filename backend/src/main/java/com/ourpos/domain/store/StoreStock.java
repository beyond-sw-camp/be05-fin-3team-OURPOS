package com.ourpos.domain.store;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.ourpos.api.storeorder.dto.request.StoreCommRequestDto;
import com.ourpos.domain.customer.CustomerAddress;
import com.ourpos.domain.storeorder.StoreComm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store_stock")
public class StoreStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_stock_id")
    private Long id;

    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @JoinColumn(name = "store_menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreComm storeComm;

    @Column(name = "store_stock_quantity")
    private Integer quantity;

    @Builder
    private StoreStock(Store store, StoreComm storeComm, Integer quantity) {
        this.store = store;
        this.storeComm = storeComm;
        this.quantity = quantity;
    }


}

