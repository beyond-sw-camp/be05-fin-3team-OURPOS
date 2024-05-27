package com.ourpos.api.storeorder.dto.response;

import com.ourpos.api.storeorder.dto.request.StoreOrderRequestDto;
import com.ourpos.domain.storeorder.StoreComm;
import com.ourpos.domain.storeorder.StoreOrder;
import com.ourpos.domain.storeorder.StoreOrderDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StoreOrderResponseDto {
	// store_comm_name,
	// store_comm_price,
	// store_comm_article_unit,
	// store_comm_picture_url,
	// store_order_detail_quantity,
	// store_order_detail_price

	private Long storeCommId;
	private Long orderDetailId;
	private String storeCommName;
	private Integer storeCommPrice; //주문 전체 가격 조회
	private String storeCommArticleUnit;

	private String storeCommPictureUrl;
	private Integer storeOrderDetailQuantity;

	private Integer storeOrderDetailPrice;//주문 메뉴 개별 가격 조회


	public StoreOrderResponseDto( StoreOrderDetail storeOrderDetail) {
		this.orderDetailId=storeOrderDetail.getId();
		this.storeCommId= storeOrderDetail.getStoreMenu().getId();
		this.storeCommName= storeOrderDetail.getStoreMenu().getName();
		this.storeCommPrice=storeOrderDetail.getStoreMenu().getPrice();
		this.storeCommArticleUnit=storeOrderDetail.getStoreMenu().getArticleUnit();
		this.storeCommPictureUrl=storeOrderDetail.getStoreMenu().getPictureUrl();
		this.storeOrderDetailQuantity=storeOrderDetail.getStoreOrder().getQuantity();
		this.storeOrderDetailPrice=storeOrderDetail.getStoreOrder().getPrice();
	}

}
