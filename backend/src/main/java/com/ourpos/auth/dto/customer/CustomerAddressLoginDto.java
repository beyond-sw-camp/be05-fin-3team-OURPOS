package com.ourpos.auth.dto.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class CustomerAddressLoginDto {

    private String name;
    private String receiverName;
    private String baseAddress;
    private String detailAddress;
    private String zipcode;
    private String receiverTelNo;
    private Boolean defaultYn;

    @Builder
    public CustomerAddressLoginDto(String name, String receiverName, String baseAddress, String detailAddress,
        String zipcode, String receiverTelNo, Boolean defaultYn) {
        this.name = name;
        this.receiverName = receiverName;
        this.baseAddress = baseAddress;
        this.detailAddress = detailAddress;
        this.zipcode = zipcode;
        this.receiverTelNo = receiverTelNo;
        this.defaultYn = defaultYn;
    }
}
