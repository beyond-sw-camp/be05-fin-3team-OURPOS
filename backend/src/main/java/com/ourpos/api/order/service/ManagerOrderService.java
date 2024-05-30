package com.ourpos.api.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ourpos.api.order.dto.response.DeliveryOrderResponseDto;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;

@Service
public interface ManagerOrderService {

    // 홀 주문 목록 확인
    Page<HallOrderResponseDto> findHallOrder(Long storeId, String status, Pageable pageable);

    Page<DeliveryOrderResponseDto> findDeliveryOrder(Long storeId, String status, Pageable pageable);

}
