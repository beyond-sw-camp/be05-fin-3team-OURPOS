package com.ourpos.api.order.request;

import java.util.ArrayList;
import java.util.List;

import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.orderdetail.OrderDetail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailRequestDto {
    private Long menuId;
    private Integer quantity;
    private List<OrderOptionGroupRequestDto> orderOptionGroups = new ArrayList<>();

    public OrderDetail toEntity(Menu menu) {
        return OrderDetail.builder()
            .menu(menu)
            .quantity(quantity)
            .orderOptionGroups(orderOptionGroups.stream()
                .map(OrderOptionGroupRequestDto::toEntity)
                .toList())
            .build();
    }
}
