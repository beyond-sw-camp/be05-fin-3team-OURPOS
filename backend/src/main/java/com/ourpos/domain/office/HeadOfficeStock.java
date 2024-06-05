package com.ourpos.domain.office;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.ourpos.domain.storeorder.StoreComm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "head_office_stock")
public class HeadOfficeStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "head_office_stock_id")
    private Long id;

    @JoinColumn(name = "store_menu_id")
    @OneToOne(fetch = FetchType.LAZY)
    private StoreComm storeComm;

    @Column(name = "head_office_stock_quantity")
    private Integer quantity;

    @Builder
    private HeadOfficeStock(StoreComm storeMenu, Integer quantity) {
        this.storeComm = storeMenu;
        this.quantity = quantity;
    }
}
