package com.ourpos.api.storeorder.dto.request;

import com.ourpos.domain.store.Store;
import com.ourpos.domain.storeorder.StoreComm;
import com.ourpos.domain.storeorder.StoreOrderDetail;
import com.ourpos.domain.storeorder.StoreOrderStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StoreOrderRequestDto {

    //order_detail_id, store_order_detail_quantity
    @NotNull(message = "가게 물품 Id를 입력해주세요")
    @Positive(message = "1 이상의 상수를 입력해주세요")
    private Long storeCommId;

    @NotNull(message = "가게 Id를 입력해주세요")
    @Positive(message = "1 이상의 상수를 입력해주세요")
    private Long storeId;

    @NotNull(message = "주문 상세 수량을 입력해주세요")
    private Integer storeOrderDetailQuantity;

    private StoreOrderStatus storeOrderStatus;


    private Integer storeCommPrice;


    public StoreOrderRequestDto(StoreOrderDetail storeOrderDetail, StoreComm storeComm) {
        this.storeCommId = storeOrderDetail.getStoreMenu().getId();
        this.storeOrderDetailQuantity = storeOrderDetail.getStoreOrder().getQuantity();
        this.storeOrderStatus = storeOrderDetail.getStoreOrder().getStatus();
        this.storeCommPrice = storeComm.getPrice();
        this.storeId= storeOrderDetail.getStoreOrder().getStore().getId();


    }

}
