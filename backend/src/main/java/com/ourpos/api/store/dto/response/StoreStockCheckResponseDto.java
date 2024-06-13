package com.ourpos.api.store.dto.response;

import static com.ourpos.domain.store.QStore.store;



import com.ourpos.domain.store.StoreStock;
import com.ourpos.domain.storeorder.StoreOrder;
import com.ourpos.domain.storeorder.StoreOrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreStockCheckResponseDto {
     //storeid,storename,재고이름,재고량이 포함되어야함

    private Long storeId;
    private String storeName;
    private String stockName;
    private Integer stockAmount;
    

    public StoreStockCheckResponseDto(StoreStock storeStock) {
        this.storeId = storeStock.getStore().getId();
        this.storeName = storeStock.getStore().getName();
        this.stockName =  storeStock.getStoreComm().getName();
        this.stockAmount = storeStock.getQuantity();
        
    }
    
}
