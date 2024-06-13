package com.ourpos.api.order.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.ourpos.api.order.service.AdminOrderService;
import com.ourpos.api.store.Location;
import com.ourpos.auth.dto.manager.ManagerUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    // 홀 -> 상태에 따른 주문 목록 확인
    @GetMapping("/orders/hall/my")
    public Result<Page<HallOrderResponseDto>> findHallOrder(@RequestParam(required = false) String status,
        Pageable pageable) {
        String adminLoginId = getManagerLoginId();

        log.info("주문 상태에 따른 주문 목록 확인 {}", status);
        Page<HallOrderResponseDto> hallOrders = adminOrderService.findHallOrder(adminLoginId, status, pageable);

        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 홀 주문 상태에 따른 주문(전체, 대기, 조리, 완료) 조회", hallOrders);
    }

    // 배달 주문 목록 확인 ( 전체, 대기, 조리중, 배달중, 완료 )
    @GetMapping("orders/delivery/my")
    public Result<Page<DeliveryOrderResponseDto>> findDeliveryOrder(@RequestParam(required = false) String status,
        Pageable pageable) {
        String adminLoginId = getManagerLoginId();

        Page<DeliveryOrderResponseDto> deliveryOrders = adminOrderService.findDeliveryOrder(adminLoginId, status,
            pageable);

        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 배달 주문 목록(전체, 대기, 조리, 배달중, 완료)를 조회", deliveryOrders);
    }

    @GetMapping("orders/monthly/my")
    public Result<List<CountMonthlyResponseDto>> countMonthly() {
        String adminLoginId = getManagerLoginId();

        List<CountMonthlyResponseDto> dto = adminOrderService.countMonthly(adminLoginId);
        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 월 매출 확인 되었습니다.", dto);
    }

    @GetMapping("orders/meal-type/my")
    public Result<List<MealTypeResponseDto>> mealType() {
        String adminLoginId = getManagerLoginId();
        log.info("식사 유형에 따른 매출액 비율 컨트롤러 {} ", adminLoginId);

        List<MealTypeResponseDto> dtos = adminOrderService.mealType(adminLoginId);

        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 식사 유형 매출액 비율이 확인 되었습니다.", dtos);
    }

    //각 메뉴별 주문 비중
    @GetMapping("orders/menu-prefer/my")
    public Result<List<MenuPreferResponseDto>> menuPrefer() {
        String adminLoginId = getManagerLoginId();

        log.info("각 메뉴별 주문 비중 컨트롤러 {} ", adminLoginId);
        List<MenuPreferResponseDto> dtos = adminOrderService.menuPrefer(adminLoginId);

        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 각 메뉴별 주문 비중", dtos);
    }

    // 시간대별 매출 발생 추이
    @GetMapping("orders/meal-time/my")
    public Result<List<MealTimeResponseDto>> mealTime() {
        String adminLoginId = getManagerLoginId();

        log.info("시간대별 매출 발생 추이 컨트롤러 {} ", adminLoginId);
        List<MealTimeResponseDto> dtos = adminOrderService.mealTime(adminLoginId);

        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 시간대별 매출 발생 추이", dtos);
    }

    // 지역별 배달 빈도
    @GetMapping("orders/delivery/frequency/my")
    public Result<List<Location>> deliveryFrequency() {
        String adminLoginId = getManagerLoginId();

        log.info("지역별 배달 빈도 컨트롤러 {} ", adminLoginId);
        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 지역별 배달 빈도수 위도 경도",
            adminOrderService.deliveryFrequency(adminLoginId));
    }

    private String getManagerLoginId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ManagerUserDetails managerUserDetails = (ManagerUserDetails)principal;

        return managerUserDetails.getUsername();
    }
}
