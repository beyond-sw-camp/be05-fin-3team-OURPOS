package com.ourpos.api.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.order.dto.response.DeliveryOrderResponseDto;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;
import com.ourpos.domain.order.DeliveryOrder;
import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.order.OrderQueryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;

    public HallOrderResponseDto findHallOrder(Long orderId) {
        HallOrder hallOrder = orderQueryRepository.findOneHallOrder(orderId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
        return new HallOrderResponseDto(hallOrder);
    }

    public DeliveryOrderResponseDto findDeliveryOrder(Long orderId) {
        DeliveryOrder deliveryOrder = orderQueryRepository.findOneDeliveryOrder(orderId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
        return new DeliveryOrderResponseDto(deliveryOrder);
    }
}
