package com.ourpos.api.storeorder.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.storeorder.dto.request.StoreOrderRequestDto;
import com.ourpos.api.storeorder.dto.response.StoreCommResponseDto;
import com.ourpos.api.storeorder.dto.response.StoreOrderResponseDto;
import com.ourpos.api.storeorder.service.StoreCommServiceImpl;
import com.ourpos.api.storeorder.service.StoreOrderServiceImpl;
import com.ourpos.domain.storeorder.StoreOrderDetail;
import com.ourpos.domain.storeorder.StoreOrderDetailRepository;
import com.ourpos.domain.storeorder.StoreOrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/storeorder")
public class StoreOrderController {

    private final StoreOrderServiceImpl storeOrderService;
    private final StoreCommServiceImpl storeCommService;
    private final StoreOrderDetailRepository storeOrderDetailRepository;
    private final StoreOrderRepository storeOrderRepository;

    // 판매 비품, 식자재 목록 확인
    @GetMapping("/checkstorecomms")
    public Result<List<StoreCommResponseDto>> checkStoreComms() {
        log.info("비품/식자재 목록 출력");
        List<StoreCommResponseDto> storeCommsList = storeCommService.getStoreComms();
        return new Result<>(HttpStatus.OK.value(), " 목록을 불러옵니다.", storeCommsList);
    }

    // 비품, 식자재 주문 (비품,식자재 주문 관리에서 배달완료 시 재고에 반영) ?-> price가 반영 안됨
    @PostMapping("/storecommorder")
    public Result<Void> createStoreOrder(@RequestBody StoreOrderRequestDto storeOrderRequestDto) {
        log.info("가게 식자재, 비품 주문: {}", storeOrderRequestDto);
        storeOrderService.createStoreOrder(storeOrderRequestDto);
        return new Result<>(HttpStatus.CREATED.value(), "식자재, 비품 주문이 완료되었습니다.", null);
    }

    // 비품, 식자재 주문 내역 확인 ? 주문 내역  개별 조회 o

    @GetMapping("/{orderDetailId}")
    public Result<StoreOrderResponseDto> getStoreOrder(@PathVariable Long orderDetailId) {
        log.info("가게 식자재, 비품 주문: {}", orderDetailId);
        StoreOrderResponseDto storeOrderResponseDto = storeOrderService.getStoreOrder(orderDetailId);
        return new Result<>(HttpStatus.OK.value(), "식자재, 비품 주문이 완료되었습니다.", storeOrderResponseDto);
    }

    // 비품, 식자재 주문 수정
    @PutMapping("/{orderdetailId}")
    public Result<Void> updateStoreOrder(@PathVariable Long orderdetailId,
        @RequestBody StoreOrderRequestDto requestDto) {
        log.info("가게 식자재, 비품 주문 수정: {}", orderdetailId);
        storeOrderService.updateStoreOrder(orderdetailId, requestDto);
        return new Result<>(HttpStatus.OK.value(), "주문 수정 성공", null);
    }

    // 비품, 식자재 주문 삭제
    @DeleteMapping("/{orderdetailId}")
    public Result<Void> deleteStoreOrder(@PathVariable Long orderdetailId) {

        log.info("비품/식자재 삭제 : {}", orderdetailId);
        StoreOrderDetail storeOrderDetail = storeOrderDetailRepository.findById(orderdetailId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));
        // storeorderdetail 삭제
        storeOrderDetailRepository.delete(storeOrderDetail);
        // storeorder 삭제
        if (storeOrderDetail.getStoreOrder() != null) {
            storeOrderRepository.delete(storeOrderDetail.getStoreOrder());
        }
        return new Result<>(HttpStatus.OK.value(), "주문이 삭제되었습니다.", null);
    }

}
