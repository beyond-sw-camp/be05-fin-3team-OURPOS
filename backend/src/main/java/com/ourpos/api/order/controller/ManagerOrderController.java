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

    // 주문 상세는 OrderQueryService
    private final OrderQueryService orderQueryService;
    // 이 외의 조회는 ManagerOrderService에서 조회
    private final ManagerOrderService managerOrderService;
    // 상태 변경은 OrderService
    private final OrderService orderService;

    // 홀 -> 상태에 따른 주문 목록 확인
    @GetMapping("/orders/hall")
    public Result<List<HallOrderResponseDto>> findHallOrder(@RequestParam Long storeId,
        @RequestParam(required = false) String status) {

        log.info("주문 상태에 따른 주문 목록 확인", storeId, status);
        List<HallOrderResponseDto> list = managerOrderService.findHallOrder(storeId, status);

        return new Result<>(HttpStatus.OK.value(), "지점용 POS에서, (전체, 대기, 조리, 완료)를 조회할 수 있다.", list);
    }

    // 홀 주문 상세 확인
    @GetMapping("/orders/hall/{orderId}")
    public Result<HallOrderResponseDto> findOneHall(@PathVariable Long orderId) {

        log.info("주문 상세 확인", orderId);
        HallOrderResponseDto hallOrderResponseDto = orderQueryService.findHallOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "지점용 POS에서, 메뉴 상세를 확인합니다.", hallOrderResponseDto);
    }

    // 홀 대기 주문 승인 - 주문 상태 ( 대기 -> 조리중 )
    @PutMapping("/orders/hall/{orderId}/accept")
    public void acceptHallOrder(@PathVariable Long orderId) {

        log.info("대기 주문 승인", orderId);
        orderService.acceptHallOrder(orderId);

    }

    // 홀 대기 주문 거절 - 주문 상태 ( 대기 -> 거절 )
    @PutMapping("/orders/hall/{orderId}/cancel")
    public void cancelHallOrder(@PathVariable Long orderId) {

        log.info("대기 주문 거절", orderId);
        orderService.cancelHallOrder(orderId);

    }

    // 홀 완료 상태변경 ( 조리중 -> 완료 )
    @PutMapping("/orders/hall/{orderId}/complete")
    public void completeHallOrder(@PathVariable Long orderId) {
        log.info("조리 주문 완료", orderId);
        orderService.completeHallOrder(orderId);
    }

    // 배달 주문 목록 확인 ( 전체, 대기, 조리중, 배달중, 완료 )
    @GetMapping("orders/delivery")
    public Result<List<DeliveryOrderResponseDto>> findDeliveryOrder(@RequestParam Long storeId,
        @RequestParam(required = false) String status) {
        log.info("배달 상태에 따른 목록 조회 : ", storeId, status);
        List<DeliveryOrderResponseDto> list = managerOrderService.findDeliveryOrder(storeId, status);

        return new Result<>(HttpStatus.OK.value(), "지점용 POS에서, 배달 (전체, 대기, 조리, 완료)를 조회할 수 있다.", list);
    }

    // 배달 각 주문 상세 확인
    @GetMapping("orders/delivery/{orderId}")
    public Result<DeliveryOrderResponseDto> findOneDelivery(@PathVariable Long orderId) {
        log.info("배달 주문 조회: {}", orderId);
        DeliveryOrderResponseDto deliveryOrder = orderQueryService.findDeliveryOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "배달 주문 조회가 완료되었습니다.", deliveryOrder);
    }

    // 배달 주문 승인 ( 대기 -> 조리중 )
    @PutMapping("orders/delivery/{orderId}/accept")
    public void acceptDeliveryOrder(@PathVariable Long orderId) {
        log.info("배달 대기 주문 승인 컨트롤러 : ", orderId);
        orderService.acceptDeliveryOrder(orderId);
    }

    // 배달 주문 거절 ( 대기 -> 거절 )
    @PutMapping("orders/delivery/{orderId}/cancel")
    public void cancelDeliveryOrder(@PathVariable Long orderId) {
        log.info("배달 대기 주문 거절 컨트롤러 : ", orderId);
        orderService.cancelDeliveryOrder(orderId);
    }

    // 배달 라이더 호출 ( 조리중 -> 배달중 )
    @PutMapping("orders/delivery/{orderId}/deliver")
    public void deliverDeliveryOrder(@PathVariable Long orderId) {
        log.info("배달 라이더 호출 컨트롤러 : ", orderId);
        orderService.startDelivery(orderId);
    }

    // 라이더 배달 완료 ( 배달중 -> 배송 완료 )
    @PutMapping("orders/delivery/{orderId}/complete")
    public void completeDeliveryOrder(@PathVariable Long orderId) {
        log.info("배달 라이더 배송 완료 컨트롤러 : ", orderId);
        orderService.completeDeliveryOrder(orderId);
    }

}
