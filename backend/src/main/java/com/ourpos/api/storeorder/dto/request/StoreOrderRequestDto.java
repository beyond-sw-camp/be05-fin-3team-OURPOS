package com.ourpos.api.storeorder.dto.request;

import java.util.List;


import com.ourpos.api.order.dto.request.OrderOptionGroupRequestDto;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.storeorder.StoreOrder;
import com.ourpos.domain.storeorder.StoreOrderDetail;
import com.ourpos.domain.storeorder.StoreOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StoreOrderRequestDto {

	//order_detail_id, store_order_detail_quantity
	private Long storeCommId;
	private Integer storeOrderDetailQuantity;
	private StoreOrderStatus storeOrderStatus;


	public StoreOrderRequestDto(StoreOrderDetail storeOrderDetail){
		this.storeCommId = storeOrderDetail.getStoreMenu().getId();
		this.storeOrderDetailQuantity = storeOrderDetail.getStoreOrder().getQuantity();
		this.storeOrderStatus = storeOrderDetail.getStoreOrder().getStatus();

	}







}
