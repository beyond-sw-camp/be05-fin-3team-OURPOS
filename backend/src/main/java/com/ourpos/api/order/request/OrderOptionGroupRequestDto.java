package com.ourpos.api.order.request;

import java.util.ArrayList;
import java.util.List;

import com.ourpos.domain.orderdetail.OrderOptionGroup;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class OrderOptionGroupRequestDto {
    private String optionGroupName;
    private List<OrderOptionRequestDto> orderOptions = new ArrayList<>();

    public OrderOptionGroup toEntity() {
        return OrderOptionGroup.builder()
            .name(optionGroupName)
            .orderOptions(orderOptions.stream()
                .map(OrderOptionRequestDto::toEntity)
                .toList())
            .build();
    }
}
