package com.ourpos.api.customer.dto.response;

import com.ourpos.domain.customer.CustomerAddress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerAddressResponseDto {

    private Long customerAddressId;
    private String receiverName;
    private String telNo;
    private String addressBase;
    private String addressDetail;
    private String zipcode;

    public CustomerAddressResponseDto(CustomerAddress customerAddress) {
        this.customerAddressId = customerAddress.getId();
        this.receiverName = customerAddress.getReceiverName();
        this.telNo = customerAddress.getTelNo();
        this.addressBase = customerAddress.getAddressBase();
        this.addressDetail = customerAddress.getAddressDetail();
        this.zipcode = customerAddress.getZipcode();
    }

}
