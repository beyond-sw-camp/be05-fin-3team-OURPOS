package com.ourpos.api.order.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderOptionRequestDto {
    private String optionName;
    private Integer price;
}
