package com.ourpos.api.order.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountMonthlyResponseDto {

    private Integer year;
    private Integer month;
    private Integer total;

    public CountMonthlyResponseDto(Integer year, Integer month, Integer total) {
        this.total = total;
        this.month = month;
        this.year = year;
    }
}
