package com.ourpos.api.storeorder.dto.response;

import com.ourpos.domain.storeorder.StoreOrder;
import com.ourpos.domain.storeorder.StoreOrderDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StoreOrderCheckResponseDto {

    private Long storeId;
    private String storeCommName;
    private Integer storeCommPrice; //주문 전체 가격 조회
    private String storeCommArticleUnit;
    private String storeCommPictureUrl;
    private Integer storeOrderDetailQuantity;
    private Integer storeOrderDetailPrice;//주문 메뉴 개별 가격 조회

    public void StoreOrderCheckResponseDto(StoreOrder storeOrder, StoreOrderDetail storeOrderDetail) {
        this.storeId = storeOrder.getStore().getId();
        this.storeCommName = storeOrderDetail.getStoreMenu().getName();
        //this.storeCommPrice = storeOrderDetail.getStoreMenu().getPrice();
        this.storeCommArticleUnit = storeOrderDetail.getStoreMenu().getArticleUnit();
        this.storeCommPictureUrl = storeOrderDetail.getStoreMenu().getPictureUrl();
        this.storeCommPrice = storeOrderDetail.getStoreOrder().getQuantity();
        this.storeOrderDetailPrice = storeOrderDetail.getStoreOrder().getPrice();

    }
}



