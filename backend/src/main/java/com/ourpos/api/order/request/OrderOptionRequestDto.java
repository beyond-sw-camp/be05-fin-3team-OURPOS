package com.ourpos.api.order.request;

import com.ourpos.domain.orderdetail.OrderOption;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderOptionRequestDto {
    private String optionName;
    private Integer price;

    public OrderOption toEntity() {
        return OrderOption.builder()
            .name(optionName)
            .price(price)
            .build();
    }
}
