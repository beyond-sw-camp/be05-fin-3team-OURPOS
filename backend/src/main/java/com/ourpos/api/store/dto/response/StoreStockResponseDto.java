package com.ourpos.api.store.dto.response;

import java.time.format.DateTimeFormatter;

import com.ourpos.domain.store.StoreStock;
import com.ourpos.domain.storeorder.StoreOrder;
import com.ourpos.domain.storeorder.StoreOrderStatus;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class StoreStockResponseDto {



    private Long storeId;
    private String storeName;
    private String stockName;
    private Integer stockAmount;
    private StoreOrderStatus status;
    
    //주문일자 
    private String orderdate;
    //배송시작일자
    private String deliverydate;


    public StoreStockResponseDto(StoreOrder storeOrder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.storeId = storeOrder.getStore().getId();
        this.storeName = storeOrder.getStore().getName();
        this.stockName = storeOrder.getStoreOrderDetails().get(0).getStoreMenu().getName();
        this.stockAmount = storeOrder.getQuantity();
        this.status = storeOrder.getStatus();
        this.orderdate = storeOrder.getCreatedDateTime().format(formatter);
        this.deliverydate = storeOrder.getDeliveryDate() != null ?
                            storeOrder.getDeliveryDate().format(formatter) : null;
        
    }
    
}
