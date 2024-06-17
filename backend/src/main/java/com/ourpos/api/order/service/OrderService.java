package com.ourpos.api.order.service;

import org.springframework.stereotype.Service;

import com.ourpos.api.order.dto.request.DeliveryOrderRequestDto;
import com.ourpos.api.order.dto.request.HallOrderRequestDto;

@Service
public interface OrderService {

    void createHallOrder(String loginId, HallOrderRequestDto hallOrderRequestDto);

    void createDeliveryOrder(String loginId, DeliveryOrderRequestDto deliveryOrderRequestDto);

    void cancelHallOrder(Long orderId);

    void acceptHallOrder(Long orderId);

    void completeHallOrder(Long orderId);

    void cancelDeliveryOrder(Long orderId);

    void acceptDeliveryOrder(Long orderId);

    void startDelivery(Long orderId);

    void completeDeliveryOrder(Long orderId);
}
