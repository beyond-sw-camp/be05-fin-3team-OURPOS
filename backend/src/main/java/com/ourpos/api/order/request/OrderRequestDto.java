package com.ourpos.api.order.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {
    private Long customerId;
    private Long storeId;
    private Integer price;
    private Boolean orderTakeoutYn;
    private List<OrderDetailRequestDto> orderDetails;
}
