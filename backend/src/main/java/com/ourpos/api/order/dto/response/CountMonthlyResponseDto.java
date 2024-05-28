package com.ourpos.api.order.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountMonthlyResponseDto {

    private Long total;

    private String month;

    public CountMonthlyResponseDto(Long total, String month) {
        this.total = total;
        this.month = month;
    }

}
