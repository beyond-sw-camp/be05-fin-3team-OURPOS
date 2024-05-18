package com.ourpos.domain.storeorder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StoreOrderDetail {

    @Id
    @Column(name = "store_order_detail_id")
    private Long id;

    @JoinColumn(name = "store_order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreOrder storeOrder;

    @JoinColumn(name = "store_menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreMenu storeMenu;

}
