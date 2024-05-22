package com.ourpos.api.order.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.ourpos.domain.orderdetail.OrderOptionGroup;

import lombok.Getter;

@Getter
public class OrderOptionGroupResponseDto {

    private Long orderOptionGroupId;
    private String optionGroupName;
    private List<OrderOptionResponseDto> orderOptionResponseDtos = new ArrayList<>();

    public OrderOptionGroupResponseDto(OrderOptionGroup orderOptionGroup) {
        this.orderOptionGroupId = orderOptionGroup.getId();
        this.optionGroupName = orderOptionGroup.getName();
        this.orderOptionResponseDtos = orderOptionGroup.getOrderOptions().stream()
            .map(OrderOptionResponseDto::new)
            .toList();
    }
}
