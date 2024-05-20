package com.ourpos.api.order.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderOptionGroupRequestDto {
    private String optionGroupName;
    private List<OrderOptionRequestDto> orderOptions;
}
