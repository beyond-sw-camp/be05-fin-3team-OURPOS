package com.ourpos.api.order.dto.response;

import java.util.List;

import com.ourpos.domain.orderdetail.OrderDetail;

import lombok.Getter;

@Getter
public class OrderDetailResponseDto {

    private Long orderDetailId;
    private Long menuId;
    private String menuName;
    private Integer menuPrice;
    private String menuPictureUrl;
    private String menuCategoryName;
    private Integer quantity;
    private Integer price;
    private List<OrderOptionGroupResponseDto> orderOptionGroupResponseDtos;

    public OrderDetailResponseDto(OrderDetail orderDetail) {
        this.orderDetailId = orderDetail.getId();
        this.menuId = orderDetail.getMenu().getId();
        this.menuName = orderDetail.getMenu().getName();
        this.menuPrice = orderDetail.getMenu().getPrice();
        this.menuPictureUrl = orderDetail.getMenu().getPictureUrl();
        this.menuCategoryName = orderDetail.getMenu().getCategory().getName();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getPrice();
        this.orderOptionGroupResponseDtos = orderDetail.getOrderOptionGroups().stream()
            .map(OrderOptionGroupResponseDto::new)
            .toList();
    }
}
