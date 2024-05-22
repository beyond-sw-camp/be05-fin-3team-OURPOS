package com.ourpos.api.order.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDto {

    private List<OrderDetailResponseDto> orderDetails = new ArrayList<>();
    private Integer order_price;
    private Date order_created_datetime;
    private String orders_payment_method;

    // public OrderResponseDto(Order order) {
    //     this.order_price = order.getPrice();
    // }

}