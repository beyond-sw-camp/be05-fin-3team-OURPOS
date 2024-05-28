package com.ourpos.domain.storeorder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store_order_detail")
@Setter
public class StoreOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_order_detail_id")
    private Long id;

    @JoinColumn(name = "store_order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreOrder storeOrder;

    @JoinColumn(name = "store_comm_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreComm storeMenu;

    @Builder
    public StoreOrderDetail(StoreOrder storeOrder, StoreComm storeMenu) {
        this.storeOrder = storeOrder;
        this.storeMenu = storeMenu;
    }

}
