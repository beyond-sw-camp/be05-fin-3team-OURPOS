package com.ourpos.api.storeorder.dto.response;

import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreAddress;
import com.ourpos.domain.storeorder.StoreCommStatus;
import com.ourpos.domain.storeorder.StoreOrder;
import com.ourpos.domain.storeorder.StoreOrderDetail;
import com.ourpos.domain.storeorder.StoreOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StoreOrderCheckResponseDto {
	//주문번호 주문일시 주문 금액 주문상태 주문 변경(주문 삭제버튼) 
	  private Long storeOrderId; 
	  private String storeOrderDate; 
	  private Integer storeOrderPrice;
	  private StoreOrderStatus storeOrderStatus;
	//주문주소 주문지점명 연락처
	  private String addressBase;
	  private String addressDetail;
	  private String zipcode;
	  private String storeName;
	  private String storePhone;
	//
	private Long storeId;
	private String storeCommName;
	private Integer storeCommPrice; //주문 전체 가격 조회
	private String storeCommArticleUnit;
	private String storeCommPictureUrl;
	private Integer storeOrderDetailQuantity;
	private Integer storeOrderDetailPrice;//주문 메뉴 개별 가격 조회




	public StoreOrderCheckResponseDto(StoreOrder storeOrder, StoreOrderDetail storeOrderDetail){
		this.storeId = storeOrder.getStore().getId();
		this.storeCommName= storeOrderDetail.getStoreMenu().getName();
		//this.storeCommPrice = storeOrderDetail.getStoreMenu().getPrice();
		this.storeCommArticleUnit= storeOrderDetail.getStoreMenu().getArticleUnit();
		this.storeCommPictureUrl = storeOrderDetail.getStoreMenu().getPictureUrl();
		this.storeCommPrice = storeOrderDetail.getStoreOrder().getQuantity();
		this.storeOrderDetailPrice = storeOrderDetail.getStoreOrder().getPrice();
		this.storeOrderDetailQuantity = storeOrderDetail.getStoreOrder().getQuantity();
		// 추가
		this.storeOrderId = storeOrder.getId();
		this.storeOrderDate = storeOrder.getCreatedDateTime().toString();
		this.storeOrderPrice = storeOrder.getPrice();
		this.storeOrderStatus = storeOrder.getStatus();
		// 2차 추가
		this.addressBase = storeOrder.getStore().getAddress().getAddressBase();
		this.addressDetail = storeOrder.getStore().getAddress().getAddressDetail();
		this.zipcode = storeOrder.getStore().getAddress().getZipcode();
		this.storeName = storeOrder.getStore().getName();
		this.storePhone = storeOrder.getStore().getPhone();


	}
}



