package com.ourpos.api.storeorder.dto.request;

import com.ourpos.domain.storeorder.StoreComm;
import com.ourpos.domain.storeorder.StoreOrderDetail;
import com.ourpos.domain.storeorder.StoreOrderStatus;

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
	private Long storeCommId;
	private Integer storeOrderDetailQuantity;
	private StoreOrderStatus storeOrderStatus;
	private Integer storeCommPrice;



	public StoreOrderRequestDto(StoreOrderDetail storeOrderDetail, StoreComm storeComm){
		this.storeCommId = storeOrderDetail.getStoreMenu().getId();
		this.storeOrderDetailQuantity = storeOrderDetail.getStoreOrder().getQuantity();
		this.storeOrderStatus = storeOrderDetail.getStoreOrder().getStatus();
		this.storeCommPrice = storeComm.getPrice();

	}







}
