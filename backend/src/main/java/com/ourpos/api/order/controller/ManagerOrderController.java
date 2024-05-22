package com.ourpos.api.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;
import com.ourpos.api.order.service.ManagerOrderService;
import com.ourpos.api.order.service.OrderQueryService;
import com.ourpos.api.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager")
public class ManagerOrderController {

    private final OrderQueryService orderQueryService;

    private final ManagerOrderService managerOrderService;

    private final OrderService orderService;

    // 대기 상태인 주문 목록 확인
    @GetMapping("/orders/waiting/{storeId}")
    public ResponseEntity<List<HallOrderResponseDto>> checkWaitingOrder(@PathVariable Long storeId) {
        System.out.println("ManagerOrderController.checkWaitingOrder");
        List<HallOrderResponseDto> list = managerOrderService.checkWaitingOrder(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    // 주문 상세 확인
    @GetMapping("/orders/hall/{orderId}")
    public Result<HallOrderResponseDto> showHallOrder(@PathVariable Long orderId) {

        System.out.println("ManagerOrderController.showHallOrder");
        HallOrderResponseDto hallOrderResponseDto = orderQueryService.findHallOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "지점용 POS에서, 메뉴 상세를 확인합니다.", hallOrderResponseDto);
    }

    // 대기 주문 승인 - 주문 상태 ( 대기 -> 조리중 )
    @PutMapping("/orders/hall/accept/{orderId}")
    public void acceptHallOrder(@PathVariable Long orderId) {
        System.out.println("ManagerOrderController.acceptHallOrder");
        orderService.acceptHallOrder(orderId);
    }

    // 대기 주문 거절 - 주문 상태 ( 대기 -> 거절 )
    @PutMapping("/orders/hall/cancel/{orderId}")
    public void cancelHallOrder(@PathVariable Long orderId) {
        System.out.println("ManagerOrderController.cancelHallOrder");
        orderService.cancelHallOrder(orderId);
    }

    // 조리중 주문 목록 확인
    @GetMapping("/orders/cooking/{storeId}")
    public ResponseEntity<List<HallOrderResponseDto>> checkCookingOrder(@PathVariable Long storeId) {
        System.out.println("ManagerOrderController.checkCookingOrder");
        List<HallOrderResponseDto> list = managerOrderService.checkCookingOrder(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    // 완료 상태변경 ( 조리중 -> 완료 )
    @PutMapping("/orders/hall/complete/{orderId}")
    public void completeHallOrder(@PathVariable Long orderId) {
        System.out.println("ManagerOrderController.completeHallOrder");
        orderService.completeHallOrder(orderId);
    }

    // 완료 주문 목록 조회
    @GetMapping("/orders/complete/{storeId}")
    public ResponseEntity<List<HallOrderResponseDto>> checkCompleteOrder(@PathVariable Long storeId) {
        System.out.println("ManagerOrderController.checkCompleteOrder");
        List<HallOrderResponseDto> list = managerOrderService.checkCompleteOrder(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

}
