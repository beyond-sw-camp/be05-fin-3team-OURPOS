package com.ourpos.api.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ourpos.api.order.request.OrderRequestDto;
import com.ourpos.api.order.response.OrderResponseDto;

@Service
public interface OrderService {

    void createHallOrder(OrderRequestDto orderRequestDto);

    void createDeliveryOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto findOne(Long orderId);

    List<OrderResponseDto> findAll();
}
