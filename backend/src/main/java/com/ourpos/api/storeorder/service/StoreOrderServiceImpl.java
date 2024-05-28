package com.ourpos.api.storeorder.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.storeorder.dto.request.StoreOrderRequestDto;
import com.ourpos.api.storeorder.dto.response.StoreCommResponseDto;
import com.ourpos.api.storeorder.dto.response.StoreOrderResponseDto;
import com.ourpos.domain.storeorder.StoreComm;
import com.ourpos.domain.storeorder.StoreCommRepository;
import com.ourpos.domain.storeorder.StoreOrder;
import com.ourpos.domain.storeorder.StoreOrderDetail;
import com.ourpos.domain.storeorder.StoreOrderDetailRepository;
import com.ourpos.domain.storeorder.StoreOrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class StoreOrderServiceImpl {

    private final StoreOrderRepository storeOrderRepository;
    private final StoreOrderDetailRepository storeOrderDetailRepository;
    private final StoreCommRepository storeCommRepository;
    //private final StoreComm storeComm;

    // 판매 비품, 식자재 목록 확인
    public List<StoreCommResponseDto> checkStoreComms() {
        System.out.println("StoreCommServiceImplServiceImpl.getStoreComms");
        List<StoreComm> storeComms = storeCommRepository.findAll();
        List<StoreCommResponseDto> storeCommResponseDtos = new ArrayList<>();

        for (StoreComm storeComm : storeComms) {
            storeCommResponseDtos.add(new StoreCommResponseDto(storeComm));
        }
        return storeCommResponseDtos;
    }

    // 비품, 식자재 주문 (비품,식자재 주문 관리에서 배달완료 시 재고에 반영)
    public void createStoreOrder(StoreOrderRequestDto requestDto) {

        //int totalPrice = requestDto.getStoreCommPrice() * requestDto.getStoreOrderDetailQuantity();

        // StoreOrder 생성
        StoreOrder storeOrder = StoreOrder.builder()
            .price(0)
            .quantity(requestDto.getStoreOrderDetailQuantity())
            .build();

        // StoreOrder 저장
        storeOrderRepository.save(storeOrder);

        StoreComm storeComm = storeCommRepository.findById(requestDto.getStoreCommId())
            .orElseThrow(() -> new IllegalArgumentException("해당 StoreComm을 찾을 수 없습니다."));

        // StoreOrderDetail 생성
        StoreOrderDetail storeOrderDetail = StoreOrderDetail.builder()
            .storeOrder(storeOrder)
            .storeMenu(storeComm)
            .build();

        // StoreOrderDetail 저장
        storeOrderDetailRepository.save(storeOrderDetail);
    }

    // 비품, 식자재 주문 내역 확인
    public StoreOrderResponseDto getStoreOrder(Long orderDetailId) {
        StoreOrderDetail storeOrderDetail = storeOrderDetailRepository.findById(orderDetailId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        return new StoreOrderResponseDto(storeOrderDetail);

    }

    // 비품, 식자재 주문 수정
    public void updateStoreOrder(Long orderDetailId, StoreOrderRequestDto storeOrderResponseDto) {
        StoreOrderDetail storeOrderDetail = storeOrderDetailRepository.findById(orderDetailId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        // storeOrderDetail.update(storeOrderResponseDto.getStoreOrderDetailQuantity());
        storeOrderDetailRepository.save(storeOrderDetail);
    }

    // 비품, 식자재 주문 삭제
    public void deleteStoreOrder(Long orderDetailId) {
        StoreOrderDetail storeOrderDetail = storeOrderDetailRepository.findById(orderDetailId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        storeOrderDetailRepository.delete(storeOrderDetail);

    }

}
