package com.ourpos.api.order.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailRequestDto {
    private Long menuId;
    private Integer quantity;
    private Integer price;
    private List<OrderOptionGroupRequestDto> orderOptionGroups;
}
