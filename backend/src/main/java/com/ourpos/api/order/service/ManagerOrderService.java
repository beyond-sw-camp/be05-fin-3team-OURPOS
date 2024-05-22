package com.ourpos.api.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ourpos.api.order.dto.response.DeliveryOrderResponseDto;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;

@Service
public interface ManagerOrderService {

    // 홀 주문 목록 확인
    List<HallOrderResponseDto> findHallOrder(Long storeId, String status);

    List<DeliveryOrderResponseDto> checkWaitingDeliverOrder(Long storeId);

}
