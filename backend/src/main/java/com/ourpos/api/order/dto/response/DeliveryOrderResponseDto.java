package com.ourpos.api.order.dto.response;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.ourpos.domain.order.DeliveryOrder;
import com.ourpos.domain.order.DeliveryStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryOrderResponseDto {

    private Long orderId;
    private Long customerId;
    private Long storeId;
    private Integer price;
    private LocalDateTime orderCreatedDateTime;
    private DeliveryStatus deliveryOrderStatus;
    private OrderAddressResponseDto orderAddressResponseDto;
    private String ownerMessage;
    private String riderMessage;
    private Integer tip;
    private Boolean disposableYn;
    private String formattedCookingTime;
    private LocalTime orderEstimatedTime;
    private List<OrderDetailResponseDto> orderDetailResponseDtos;

    public DeliveryOrderResponseDto(DeliveryOrder order) {
        this.orderId = order.getId();
        this.customerId = order.getCustomer().getId();
        this.storeId = order.getStore().getId();
        this.price = order.getPrice();
        this.orderCreatedDateTime = order.getCreatedDateTime();
        this.deliveryOrderStatus = order.getStatus();
        this.orderAddressResponseDto = new OrderAddressResponseDto(order.getOrderAddress());
        this.ownerMessage = order.getOwnerMessage();
        this.riderMessage = order.getRiderMessage();
        this.tip = order.getTip();
        this.disposableYn = order.getDisposableYn();
        this.formattedCookingTime = formatDuration(order.getElapsedTime());
        this.orderEstimatedTime = order.getEstimatedTime();
        this.orderDetailResponseDtos = order.getOrderDetails().stream()
            .map(OrderDetailResponseDto::new)
            .toList();
    }
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
