package com.ourpos.api.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ourpos.api.order.dto.response.HallOrderResponseDto;

@Service
public interface ManagerOrderService {

    List<HallOrderResponseDto> checkWaitingOrder(Long storeId);

    List<HallOrderResponseDto> checkCookingOrder(Long storeId);

    List<HallOrderResponseDto> checkCompleteOrder(Long storeId);

}
