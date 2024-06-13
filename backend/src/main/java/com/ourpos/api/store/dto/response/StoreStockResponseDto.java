package com.ourpos.api.store.dto.response;

import com.ourpos.domain.store.StoreStock;
import com.ourpos.domain.storeorder.StoreOrder;
import com.ourpos.domain.storeorder.StoreOrderStatus;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class StoreStockResponseDto {

    //storeid,storename,재고이름,재고량이 포함되어야함

    private Long storeId;
    private String storeName;
    private String stockName;
    private Integer stockAmount;
    private StoreOrderStatus status;

    public StoreStockResponseDto(StoreOrder storeOrder) {
        this.storeId = storeOrder.getStore().getId();
        this.storeName = storeOrder.getStore().getName();
        this.stockName = storeOrder.getStoreOrderDetails().get(0).getStoreMenu().getName();
        this.stockAmount = storeOrder.getQuantity();
        this.status = storeOrder.getStatus();
    }
    
}
