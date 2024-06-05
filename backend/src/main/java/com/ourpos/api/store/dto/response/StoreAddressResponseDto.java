package com.ourpos.api.store.dto.response;

import com.ourpos.domain.store.StoreAddress;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreAddressResponseDto {

    private String addressBase;
    private String addressDetail;
    private String zipcode;
    private Double latitude;
    private Double longitude;

    public StoreAddressResponseDto(StoreAddress storeAddress) {
        this.addressBase = storeAddress.getAddressBase();
        this.addressDetail = storeAddress.getAddressDetail();
        this.zipcode = storeAddress.getZipcode();
        this.latitude = storeAddress.getLatitude();
        this.longitude = storeAddress.getLongitude();
    }
}
