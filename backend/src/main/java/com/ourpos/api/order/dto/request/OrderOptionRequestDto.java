package com.ourpos.api.order.dto.request;

import com.ourpos.domain.orderdetail.OrderOption;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
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
