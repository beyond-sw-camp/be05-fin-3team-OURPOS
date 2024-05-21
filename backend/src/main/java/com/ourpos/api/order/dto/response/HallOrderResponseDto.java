package com.ourpos.api.order.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.order.HallStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HallOrderResponseDto {

    private Long orderId;
    private Long customerId;
    private Long storeId;
    private Integer price;
    private LocalDateTime orderCreatedDateTime;
    private HallStatus hallOrderStatus;
    private Boolean orderTakeoutYn;
    private List<OrderDetailResponseDto> orderDetailResponseDtos = new ArrayList<>();

    public HallOrderResponseDto(HallOrder order) {
        this.orderId = order.getId();
        this.customerId = order.getCustomer().getId();
        this.storeId = order.getStore().getId();
        this.price = order.getPrice();
        this.orderCreatedDateTime = order.getCreatedDateTime();
        this.hallOrderStatus = order.getStatus();
        this.orderTakeoutYn = order.getOrderTakeoutYn();
        this.orderDetailResponseDtos = order.getOrderDetails().stream()
            .map(OrderDetailResponseDto::new)
            .toList();
    }
}
