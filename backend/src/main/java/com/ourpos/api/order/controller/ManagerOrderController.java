package com.ourpos.api.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.order.dto.response.DeliveryOrderResponseDto;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;
import com.ourpos.api.order.service.ManagerOrderService;
import com.ourpos.api.order.service.OrderQueryService;
import com.ourpos.api.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ManagerOrderController {

    private final OrderQueryService orderQueryService;

    private final ManagerOrderService managerOrderService;

    private final OrderService orderService;

    // 홀 -> 상태에 따른 주문 목록 확인
    @GetMapping("/orders/hall")
    public Result<List<HallOrderResponseDto>> findHallOrder(@RequestParam Long storeId,
        @RequestParam(required = false) String status) {

        log.info("주문 상태에 따른 주문 목록 확인", storeId, status);
        List<HallOrderResponseDto> list = managerOrderService.findHallOrder(storeId, status);

        return new Result<>(HttpStatus.OK.value(), "지점용 POS에서, (전체, 대기, 조리, 완료)를 조회할 수 있다.", list);
    }

    // 주문 상세 확인
    @GetMapping("/orders/hall/{orderId}/accept")
    public Result<HallOrderResponseDto> showHallOrder(@PathVariable Long orderId) {

        log.info("주문 상세 확인", orderId);
        HallOrderResponseDto hallOrderResponseDto = orderQueryService.findHallOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "지점용 POS에서, 메뉴 상세를 확인합니다.", hallOrderResponseDto);
    }

    // 대기 주문 승인 - 주문 상태 ( 대기 -> 조리중 )
    @PutMapping("/orders/hall/{orderId}/accept")
    public void acceptHallOrder(@PathVariable Long orderId) {

        log.info("대기 주문 승인", orderId);
        orderService.acceptHallOrder(orderId);

    }

    // 대기 주문 거절 - 주문 상태 ( 대기 -> 거절 )
    @PutMapping("/orders/hall/{orderId}/cancel")
    public void cancelHallOrder(@PathVariable Long orderId) {

        log.info("대기 주문 거절", orderId);
        orderService.cancelHallOrder(orderId);

    }

    // 완료 상태변경 ( 조리중 -> 완료 )
    @PutMapping("/orders/hall/{orderId}/complete")
    public void completeHallOrder(@PathVariable Long orderId) {
        log.info("조리 주문 완료", orderId);
        orderService.completeHallOrder(orderId);
    }

    // 배달 대기 주문 확인
    @GetMapping("/delivers/{storeId}/waiting")
    public Result<List<DeliveryOrderResponseDto>> checkWaitingDeliver(@PathVariable Long storeId) {
        log.info("배달 대기 주문 조회 : ", storeId);
        List<DeliveryOrderResponseDto> list = managerOrderService.checkWaitingDeliverOrder(storeId);

        return new Result<>(HttpStatus.OK.value(), "지점용 POS에서, 배달 대기 주문 목록을 확인합니다.", list);
    }

    // 배달 주문 승인 ( 대기 -> 조리중 )
    @PutMapping("/delivers/{orderId}/accept")
    public void acceptDeliverOrder(@PathVariable Long orderId) {
        log.info("배달 대기 주문 승인 컨트롤러 : ", orderId);
        orderService.acceptDeliveryOrder(orderId);
    }

    // 배달 주문 거절 ( 대기 -> 거절 )
    @PutMapping("/delivers/{orderId}/cancel")
    public void cancelDeliverOrder(@PathVariable Long orderId) {
        log.info("배달 대기 주문 거절 컨트롤러 : ", orderId);
        orderService.cancelDeliveryOrder(orderId);
    }

}
