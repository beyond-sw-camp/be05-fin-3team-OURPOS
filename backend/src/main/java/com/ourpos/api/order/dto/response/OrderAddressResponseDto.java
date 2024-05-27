package com.ourpos.api.order.dto.response;

import com.ourpos.domain.order.OrderAddress;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderAddressResponseDto {

    private Long orderAddressId;
    private String receiverName;
    private String telNo;
    private String addressBase;
    private String addressDetail;
    private String zipcode;

    public OrderAddressResponseDto(OrderAddress orderAddress) {
        this.orderAddressId = orderAddress.getId();
        this.receiverName = orderAddress.getReceiverName();
        this.telNo = orderAddress.getTelNo();
        this.addressBase = orderAddress.getAddressBase();
        this.addressDetail = orderAddress.getAddressDetail();
        this.zipcode = orderAddress.getZipcode();
    }
}
