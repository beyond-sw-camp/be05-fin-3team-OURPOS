package com.ourpos.api.order.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealTypeResponseDto {

    private String type;

    private Integer total;

    public MealTypeResponseDto(String type, Integer total) {
        this.type = type;
        this.total = total;
    }
}
