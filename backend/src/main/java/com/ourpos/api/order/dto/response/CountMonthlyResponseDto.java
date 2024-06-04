package com.ourpos.api.order.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountMonthlyResponseDto {

    private Long storeId;
    private String storeName;
    private Integer year;
    private Integer month;
    private Integer total;

    public CountMonthlyResponseDto(Long storeId, String storeName, Integer year, Integer month, Integer total) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.total = total;
        this.month = month;
        this.year = year;
    }
}
