package com.ourpos.api.order.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealTimeResponseDto {

    private Long storeId;
    private String storeName;
    private Integer hour;

    private Integer total;

    public MealTimeResponseDto(Long storeId, String storeName, Integer hour, Integer total) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.hour = hour;
        this.total = total;
    }
}
