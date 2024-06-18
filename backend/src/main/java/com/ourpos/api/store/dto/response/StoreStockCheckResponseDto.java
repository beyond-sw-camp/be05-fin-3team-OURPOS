package com.ourpos.api.store.dto.response;

import static com.ourpos.domain.store.QStore.store;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private String stockingTime;
    
    

    public StoreStockCheckResponseDto(StoreStock storeStock) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.storeId = storeStock.getStore().getId();
        this.storeName = storeStock.getStore().getName();
        this.stockName =  storeStock.getStoreComm().getName();
        this.stockAmount = storeStock.getQuantity();
        this.stockingTime = storeStock.getCreatedAt().format(formatter);
        
    }
    
}
