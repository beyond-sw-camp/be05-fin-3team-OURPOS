package com.ourpos.api.storeorder.service;

import java.util.List;

import com.ourpos.api.storeorder.dto.request.StoreOrderRequestDto;
import com.ourpos.api.storeorder.dto.response.StoreCommResponseDto;

public interface StoreOrderService {
    // 판매 비품, 식자재 목록 확인

    // 비품, 식자재 주문 (비품,식자재 주문 관리에서 배달완료 시 재고에 반영)
    //void createStoreOrder(StoreOrderRequestDto storeOrderRequestDto);
    void createOrder(Long storeOrderDetailId, Integer orderDetailQuantity);
    //

    // 비품, 식자재 주문 내역 확인
    List<StoreCommResponseDto> checkStoreComms();

    // 비품, 식자재 주문 수정
    void updateStoreOrder(Long OrderDetailId, StoreOrderRequestDto storeOrderRequestDto);

    // 비품, 식자재 주문 삭제
    void deleteStoreOrder(Long OrderDetailId);

}
