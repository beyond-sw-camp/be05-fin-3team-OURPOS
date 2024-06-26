package com.ourpos.api.order.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.order.dto.response.CountMonthlyResponseDto;
import com.ourpos.api.order.dto.response.DeliveryOrderResponseDto;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;
import com.ourpos.api.order.dto.response.MealTimeResponseDto;
import com.ourpos.api.order.dto.response.MealTypeResponseDto;
import com.ourpos.api.order.dto.response.MenuPreferResponseDto;
import com.ourpos.api.order.service.ManagerOrderService;
import com.ourpos.api.order.service.OrderQueryService;
import com.ourpos.api.order.service.OrderService;
import com.ourpos.api.store.Location;

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
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/orders/hall")
    public Result<Page<HallOrderResponseDto>> findHallOrder(@RequestParam Long storeId,
        @RequestParam(required = false) String status, Pageable pageable) {

        log.info("주문 상태에 따른 주문 목록 확인 {} {}", storeId, status);
        Page<HallOrderResponseDto> hallOrders = managerOrderService.findHallOrder(storeId, status, pageable);

        return new Result<>(HttpStatus.OK.value(), "홀  주문 상태에 따른 주문(전체, 대기, 조리, 완료)를 조회할 수 있다.", hallOrders);
    }

    // 홀 주문 상세 확인
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @GetMapping("/orders/hall/{orderId}")
    public Result<HallOrderResponseDto> findOneHall(@PathVariable Long orderId) {

        log.info("주문 상세 확인 {}", orderId);
        HallOrderResponseDto hallOrderResponseDto = orderQueryService.findHallOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "홀 주문 상세를 확인합니다.", hallOrderResponseDto);
    }

    // 홀 대기 주문 승인 - 주문 상태 ( 대기 -> 조리중 )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/orders/hall/{orderId}/accept")
    public Result<Void> acceptHallOrder(@PathVariable Long orderId) {

        log.info("대기 주문 승인 {}", orderId);
        orderService.acceptHallOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "홀 대기 승인되었습니다.", null);
    }

    // 홀 대기 주문 거절 - 주문 상태 ( 대기 -> 거절 )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/orders/hall/{orderId}/cancel")
    public Result<Void> cancelHallOrder(@PathVariable Long orderId) {

        log.info("대기 주문 거절 {}", orderId);
        orderService.cancelHallOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "홀 대기 거절되었습니다.", null);
    }

    // 홀 완료 상태변경 ( 조리중 -> 완료 )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/orders/hall/{orderId}/complete")
    public Result<Void> completeHallOrder(@PathVariable Long orderId) {
        log.info("조리 주문 완료  {}", orderId);
        orderService.completeHallOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "홀 조리 완료 되었습니다.", null);
    }

    // 배달 주문 목록 확인 ( 전체, 대기, 조리중, 배달중, 완료 )
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @GetMapping("orders/delivery")
    public Result<Page<DeliveryOrderResponseDto>> findDeliveryOrder(@RequestParam Long storeId,
        @RequestParam(required = false) String status, Pageable pageable) {
        log.info("배달 상태에 따른 목록 조회  {} {}", storeId, status);
        Page<DeliveryOrderResponseDto> deliveryOrders = managerOrderService.findDeliveryOrder(storeId, status,
            pageable);

        return new Result<>(HttpStatus.OK.value(), "배달 주문 목록(전체, 대기, 조리, 배달중, 완료)를 조회할 수 있다.", deliveryOrders);
    }

    // 배달 각 주문 상세 확인
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @GetMapping("orders/delivery/{orderId}")
    public Result<DeliveryOrderResponseDto> findOneDelivery(@PathVariable Long orderId) {
        log.info("배달 주문 조회: {}", orderId);
        DeliveryOrderResponseDto deliveryOrder = orderQueryService.findDeliveryOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "배달 각 주문 상세를 확인 완료되었습니다.", deliveryOrder);
    }

    // 배달 주문 승인 ( 대기 -> 조리중 )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("orders/delivery/{orderId}/accept")
    public Result<Void> acceptDeliveryOrder(@PathVariable Long orderId) {

        log.info("배달 대기 주문 승인 컨트롤러 {}", orderId);
        orderService.acceptDeliveryOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "배달 주문 승인 되었습니다.", null);
    }

    // 배달 주문 거절 ( 대기 -> 거절 )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("orders/delivery/{orderId}/cancel")
    public Result<Void> cancelDeliveryOrder(@PathVariable Long orderId) {

        log.info("배달 대기 주문 거절 컨트롤러 {}", orderId);
        orderService.cancelDeliveryOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "배달 주문 거절되었습니다.", null);
    }

    // 배달 라이더 호출 ( 조리중 -> 배달중 )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("orders/delivery/{orderId}/deliver")
    public Result<Void> deliverDeliveryOrder(@PathVariable Long orderId) {

        log.info("배달 라이더 호출 컨트롤러 {}", orderId);
        orderService.startDelivery(orderId);

        return new Result<>(HttpStatus.OK.value(), "배달 라이더 호출 되었습니다.", null);
    }

    // 라이더 배달 완료 ( 배달중 -> 배송 완료 )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("orders/delivery/{orderId}/complete")
    public Result<Void> completeDeliveryOrder(@PathVariable Long orderId) {

        log.info("배달 라이더 배송 완료 컨트롤러 {}", orderId);
        orderService.completeDeliveryOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "배달 완료 되었습니다.", null);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @GetMapping("orders/monthly")
    public Result<List<CountMonthlyResponseDto>> countMouthly(@RequestParam(required = false) Long storeId) {
        List<CountMonthlyResponseDto> dto = managerOrderService.countMonthly(storeId);
        return new Result<>(HttpStatus.OK.value(), "월 매출 확인 되었습니다.", dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @GetMapping("orders/meal-type")
    public Result<List<MealTypeResponseDto>> mealType(@RequestParam(required = false) Long storeId) {
        log.info("식사 유형에 따른 매출액 비율 컨트롤러 {} ", storeId);
        List<MealTypeResponseDto> dtos = managerOrderService.mealType(storeId);

        return new Result<>(HttpStatus.OK.value(), "식사 유형 매출액 비율이 확인 되었습니다.", dtos);
    }

    //각 메뉴별 주문 비중
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @GetMapping("orders/menu-prefer")
    public Result<List<MenuPreferResponseDto>> menuPrefer(@RequestParam(required = false) Long storeId) {
        log.info("각 메뉴별 주문 비중 컨트롤러 {} ", storeId);
        List<MenuPreferResponseDto> dtos = managerOrderService.menuPrefer(storeId);

        return new Result<>(HttpStatus.OK.value(), "각 메뉴별 주문 비중", dtos);
    }

    // 시간대별 매출 발생 추이
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @GetMapping("orders/meal-time")
    public Result<List<MealTimeResponseDto>> mealTime(@RequestParam(required = false) Long storeId) {
        log.info("시간대별 매출 발생 추이 컨트롤러 {} ", storeId);
        List<MealTimeResponseDto> dtos = managerOrderService.mealTime(storeId);

        return new Result<>(HttpStatus.OK.value(), "시간대별 매출 발생 추이", dtos);
    }

    // 지역별 배달 빈도
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @GetMapping("orders/delivery/frequency")
    public Result<List<Location>> deliveryFrequency(@RequestParam(required = false) Long storeId) {
        log.info("지역별 배달 빈도 컨트롤러 {} ", storeId);
        return new Result<>(HttpStatus.OK.value(), "지역별 배달 빈도수 위도 경도", managerOrderService.deliveryFrequency(storeId));
    }

}
