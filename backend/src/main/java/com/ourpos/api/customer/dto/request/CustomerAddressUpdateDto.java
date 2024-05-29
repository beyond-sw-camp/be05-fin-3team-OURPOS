package com.ourpos.api.customer.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomerAddressUpdateDto {

    private String name;
    private String receiverName;
    private String telNo;
    private String addressBase;
    private String addressDetail;
    private String zipcode;

    @Builder
    public CustomerAddressUpdateDto(String name, String receiverName, String telNo, String addressBase,
        String addressDetail, String zipcode) {
        this.name = name;
        this.receiverName = receiverName;
        this.telNo = telNo;
        this.addressBase = addressBase;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
    }
}
