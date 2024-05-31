package com.ourpos.api.store.dto.request;

import com.ourpos.domain.store.StoreAddress;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreAddressRequestDto {

    private String addressBase;
    private String addressDetail;
    private String zipcode;
    private Double latitude;
    private Double longitude;

    public StoreAddress toEntity() {
        return StoreAddress.builder()
            .addressBase(addressBase)
            .addressDetail(addressDetail)
            .zipcode(zipcode)
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }
}
