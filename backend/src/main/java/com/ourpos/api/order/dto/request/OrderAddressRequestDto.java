package com.ourpos.api.order.dto.request;

import com.ourpos.domain.order.OrderAddress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderAddressRequestDto {

    private String receiverName;
    private String telNo;
    private String addressBase;
    private String addressDetail;
    private String zipcode;

    public OrderAddress toEntity() {
        return OrderAddress.builder()
            .receiverName(receiverName)
            .telNo(telNo)
            .addressBase(addressBase)
            .addressDetail(addressDetail)
            .zipcode(zipcode)
            .build();
    }
}
