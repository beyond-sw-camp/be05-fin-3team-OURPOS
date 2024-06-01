package com.ourpos.api.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ourpos.api.order.dto.response.DeliveryOrderResponseDto;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;
import com.ourpos.domain.order.DeliveryOrder;
import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.order.OrderQueryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerOrderServiceImpl implements ManagerOrderService {

    private final OrderQueryRepository orderQueryRepository;

    // 홀 상태 주문 목록 조회
    @Override
    public Page<HallOrderResponseDto> findHallOrder(Long storeId, String status, Pageable pageable) {
        log.info("홀 상태 주문 목록 조회 서비스 {} {}", storeId, status);
        Page<HallOrder> hallOrders = orderQueryRepository.findHallOrders(storeId, status, pageable);

        return hallOrders.map(HallOrderResponseDto::new);
    }

    // 배달 상태에 따른 목록 조회
    @Override
    public Page<DeliveryOrderResponseDto> findDeliveryOrder(Long storeId, String status, Pageable pageable) {
        log.info("배달 대기 주문 조회 서비스 {} {} ", storeId, status);
        Page<DeliveryOrder> deliveryOrders = orderQueryRepository.findDeliveryOrders(storeId, status, pageable);

        return deliveryOrders.map(DeliveryOrderResponseDto::new);
    }

}
