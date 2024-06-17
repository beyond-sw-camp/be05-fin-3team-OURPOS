package com.ourpos.api.store.dto.request;

import java.time.LocalTime;

import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.store.Store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class StoreRequestDto {

    private Long customerId;
    private StoreAddressRequestDto storeAddress;
    private String storeName;
    private String storePhone;
    private LocalTime storeOpenTime;
    private LocalTime storeCloseTime;
    private Integer storeMinimumOrderPrice;
    private String storePictureUrl;

    public Store toEntity(Customer customer) {
        return Store.builder()
            .customer(customer)
            .address(storeAddress.toEntity())
            .name(storeName)
            .phone(storePhone)
            .openTime(storeOpenTime)
            .closeTime(storeCloseTime)
            .minimumOrderPrice(storeMinimumOrderPrice)
            .pictureUrl(storePictureUrl)
            .build();
    }
}
