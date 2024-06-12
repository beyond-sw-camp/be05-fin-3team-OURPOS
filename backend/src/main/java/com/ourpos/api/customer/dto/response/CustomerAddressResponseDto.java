package com.ourpos.api.customer.dto.response;

import com.ourpos.domain.customer.CustomerAddress;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerAddressResponseDto {


    private Long customerAddressId;

    private String name;

    private String receiverName;

    private String telNo;

    private String addressBase;

    private String addressDetail;

    private String zipcode;

    private Boolean defaultYn;

  

    public CustomerAddressResponseDto(CustomerAddress customerAddress) {
        this.customerAddressId = customerAddress.getId();
        this.name = customerAddress.getName();
        this.receiverName = customerAddress.getReceiverName();
        this.telNo = customerAddress.getTelNo();
        this.addressBase = customerAddress.getAddressBase();
        this.addressDetail = customerAddress.getAddressDetail();
        this.zipcode = customerAddress.getZipcode();
        this.defaultYn = customerAddress.getDefaultYn();
      
    }

}
