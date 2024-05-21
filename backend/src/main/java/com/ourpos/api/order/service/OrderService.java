package com.ourpos.api.order.service;

import org.springframework.stereotype.Service;

import com.ourpos.api.order.dto.request.DeliveryOrderRequestDto;
import com.ourpos.api.order.dto.request.HallOrderRequestDto;

@Service
public interface OrderService {

    void createHallOrder(HallOrderRequestDto hallOrderRequestDto);

    void createDeliveryOrder(DeliveryOrderRequestDto deliveryOrderRequestDto);

    void cancelHallOrder(Long orderId);

    void acceptHallOrder(Long orderId);

    void completeHallOrder(Long orderId);
}
