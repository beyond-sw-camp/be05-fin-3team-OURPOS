package com.ourpos.api.order.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealTimeResponseDto {

    private Integer hour;

    private Integer total;

    public MealTimeResponseDto(Integer hour, Integer total) {
        this.hour = hour;
        this.total = total;
    }
}
