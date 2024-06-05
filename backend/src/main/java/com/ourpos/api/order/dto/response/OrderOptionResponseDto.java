package com.ourpos.api.order.dto.response;

import com.ourpos.domain.orderdetail.OrderOption;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderOptionResponseDto {

    private Long orderOptionId;
    private String optionName;
    private Integer price;

    public OrderOptionResponseDto(OrderOption orderOption) {
        this.orderOptionId = orderOption.getId();
        this.optionName = orderOption.getName();
        this.price = orderOption.getPrice();
    }
}
