package com.ourpos.api.order.dto.request;

import com.ourpos.domain.order.OrderAddress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryAddressDto {

    private String addressSi;
    private String addressGu;
    private String addressStreetName;
    private String addressDetail;

    public OrderAddress toEntity() {
        return OrderAddress.builder()
            .addressSi(addressSi)
            .addressGu(addressGu)
            .addressStreetName(addressStreetName)
            .addressDetail(addressDetail)
            .build();
    }
}
