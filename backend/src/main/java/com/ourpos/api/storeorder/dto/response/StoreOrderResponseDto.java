package com.ourpos.api.storeorder.dto.response;

import com.ourpos.domain.storeorder.StoreOrderDetail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "가게에서 본사에 주문한 비품/ 식자재  주문 내역 ResponseDto")
public class StoreOrderResponseDto {
    // store_comm_name,
    // store_comm_price,
    // store_comm_article_unit,
    // store_comm_picture_url,
    // store_order_detail_quantity,
    // store_order_detail_price

    @Schema(description = "주문한 가게 물품의 아이디")
    private Long storeCommId;

    @Schema(description = "주문 상세 아이디")
    private Long orderDetailId;

    @Schema(description = "주문한 가게 물품 이름")
    private String storeCommName;

    @Schema(description = "주문한 가게 물품 가격")
    private Integer storeCommPrice; //주문 전체 가격 조회

    @Schema(description = "주문한 가게 물품의 단위")
    private String storeCommArticleUnit;

    @Schema(description = "주문한 가게 물품 사진")
    private String storeCommPictureUrl;

    @Schema(description = "주문한 가게 물품의 수량")
    private Integer storeOrderDetailQuantity;

    @Schema(description = "주문한 가게 물품의 총 가격")
    private Integer storeOrderDetailPrice;//주문 메뉴 개별 가격 조회

    public StoreOrderResponseDto(StoreOrderDetail storeOrderDetail) {
        this.orderDetailId = storeOrderDetail.getId();
        this.storeCommId = storeOrderDetail.getStoreMenu().getId();
        this.storeCommName = storeOrderDetail.getStoreMenu().getName();
        this.storeCommPrice = storeOrderDetail.getStoreMenu().getPrice();
        this.storeCommArticleUnit = storeOrderDetail.getStoreMenu().getArticleUnit();
        this.storeCommPictureUrl = storeOrderDetail.getStoreMenu().getPictureUrl();
        this.storeOrderDetailQuantity = storeOrderDetail.getStoreOrder().getQuantity();
        this.storeOrderDetailPrice = storeOrderDetail.getStoreOrder().getPrice();
    }

}
