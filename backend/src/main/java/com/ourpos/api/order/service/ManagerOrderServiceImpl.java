package com.ourpos.api.order.service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<HallOrderResponseDto> findHallOrder(Long storeId, String status) {
        log.info("홀 상태 주문 목록 조회 서비스 {} {}", storeId, status);
        List<HallOrder> hallOrders = orderQueryRepository.findHallOrder(storeId, status);

        return hallOrders.stream()
            .map(HallOrderResponseDto::new)
            .collect(Collectors.toList());
    }

    // 배달 상태에 따른 목록 조회
    @Override
    public List<DeliveryOrderResponseDto> findDeliveryOrder(Long storeId, String status) {
        log.info("배달 대기 주문 조회 서비스 {} {} ", storeId, status);

        List<DeliveryOrder> deliveryOrders = orderQueryRepository.findDeliveryOrder(storeId, status);

        return deliveryOrders.stream()
            .map(DeliveryOrderResponseDto::new)
            .collect(Collectors.toList());
    }

}
