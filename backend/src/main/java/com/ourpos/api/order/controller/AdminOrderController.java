package com.ourpos.api.order.controller;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.ourpos.api.order.service.AdminOrderService;
import com.ourpos.api.store.Location;
import com.ourpos.api.store.dto.response.StoreStockCheckResponseDto;
import com.ourpos.api.store.dto.response.StoreStockResponseDto;
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/orders/hall/my")
    public Result<Page<HallOrderResponseDto>> findHallOrder(@RequestParam(required = false) String status,
        Pageable pageable) {
        String adminLoginId = getManagerLoginId();

        log.info("주문 상태에 따른 주문 목록 확인 {}", status);
        Page<HallOrderResponseDto> hallOrders = adminOrderService.findHallOrder(adminLoginId, status, pageable);

        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 홀 주문 상태에 따른 주문(전체, 대기, 조리, 완료) 조회", hallOrders);
    }

    // 배달 주문 목록 확인 ( 전체, 대기, 조리중, 배달중, 완료 )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("orders/delivery/my")
    public Result<Page<DeliveryOrderResponseDto>> findDeliveryOrder(@RequestParam(required = false) String status,
        Pageable pageable) {
        String adminLoginId = getManagerLoginId();

        Page<DeliveryOrderResponseDto> deliveryOrders = adminOrderService.findDeliveryOrder(adminLoginId, status,
            pageable);

        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 배달 주문 목록(전체, 대기, 조리, 배달중, 완료)를 조회", deliveryOrders);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("orders/monthly/my")
    public Result<List<CountMonthlyResponseDto>> countMonthly() {
        String adminLoginId = getManagerLoginId();

        List<CountMonthlyResponseDto> dto = adminOrderService.countMonthly(adminLoginId);
        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 월 매출 확인 되었습니다.", dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("orders/meal-type/my")
    public Result<List<MealTypeResponseDto>> mealType() {
        String adminLoginId = getManagerLoginId();
        log.info("식사 유형에 따른 매출액 비율 컨트롤러 {} ", adminLoginId);

        List<MealTypeResponseDto> dtos = adminOrderService.mealType(adminLoginId);

        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 식사 유형 매출액 비율이 확인 되었습니다.", dtos);
    }

    //각 메뉴별 주문 비중
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("orders/menu-prefer/my")
    public Result<List<MenuPreferResponseDto>> menuPrefer() {
        String adminLoginId = getManagerLoginId();

        log.info("각 메뉴별 주문 비중 컨트롤러 {} ", adminLoginId);
        List<MenuPreferResponseDto> dtos = adminOrderService.menuPrefer(adminLoginId);

        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 각 메뉴별 주문 비중", dtos);
    }

    // 시간대별 매출 발생 추이
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("orders/meal-time/my")
    public Result<List<MealTimeResponseDto>> mealTime() {
        String adminLoginId = getManagerLoginId();

        log.info("시간대별 매출 발생 추이 컨트롤러 {} ", adminLoginId);
        List<MealTimeResponseDto> dtos = adminOrderService.mealTime(adminLoginId);

        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 시간대별 매출 발생 추이", dtos);
    }

    // 지역별 배달 빈도
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("orders/delivery/frequency/my")
    public Result<List<Location>> deliveryFrequency() {
        String adminLoginId = getManagerLoginId();

        log.info("지역별 배달 빈도 컨트롤러 {} ", adminLoginId);
        return new Result<>(HttpStatus.OK.value(), "로그인 된 가게의 지역별 배달 빈도수 위도 경도",
            adminOrderService.deliveryFrequency(adminLoginId));
    }

    // 아침에 들어온 재고 고정량 조회

    // 식자재, 비품 입고 예정량 조회 (접수완료, 대기중, 배송중)-> 본사
    /* 
    @GetMapping("/store/{storeId}/incoming-stock")
    public Result<List<StoreStockResponseDto>> getIncomingStock(@PathVariable Long storeId) {
        log.info("가게 입고 예정량 조회: {}", storeId);
        log.debug("getIncomingStock 메서드 호출됨");
        try {
            List<StoreStockResponseDto> incomingStockList = adminOrderService.getIncomingStock(storeId);
            log.debug("입고 예정량 데이터 로드 성공: {}", incomingStockList);
            return new Result<>(HttpStatus.OK.value(), "입고 예정량 목록을 불러옵니다", incomingStockList);
        } catch (Exception e) {
            log.error("입고 예정량 데이터 로드 실패", e);
            return new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "입고 예정량을 불러오는 중에 오류가 발생했습니다", null);
        }
    }
        */
        // 식자재, 비품 입고 예정량 조회 (접수완료, 대기중, 배송중)-> 직영점
        @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
        @GetMapping("/store/incoming-stock/my")
        public Result<List<StoreStockResponseDto>> getIncomingStockforstore() {
	       String adminLoginId = getManagerLoginId();

            log.info("가게 입고 예정량 조회: {}", adminLoginId);
            log.debug("getIncomingStock 메서드 호출됨");
            List<StoreStockResponseDto> incomingStockList = adminOrderService.getIncomingStock(adminLoginId);
            return new Result<>(HttpStatus.OK.value(), "입고 예정량 목록을 불러옵니다", incomingStockList);
            

        }

    // 기타 입고 (점주가 배송된 상품의 상태 확인 후 임의로 재고 변경)-> 직영점
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/storestocks/{storeStockId}/decrease")
    public ResponseEntity<String> decreaseStock(
        @PathVariable Long storeStockId,
        @RequestParam Integer quantity
    ) {
        adminOrderService.decreaseStock(storeStockId, quantity);
        return ResponseEntity.status(HttpStatus.OK).body("재고가 감소되었습니다.");
    }
    /* 
    // 배송 완료 반영된 재고량 조회 (기타 입출고 포함)-> 본사
    @GetMapping("/storestocks")
    public ResponseEntity<List<StoreStockCheckResponseDto>> getAllStoreStocks() {
        List<StoreStockCheckResponseDto> storeStocks = adminOrderService.getAllStoreStocks();
        return ResponseEntity.status(HttpStatus.OK).body(storeStocks);
    }
    */

    // 배송 완료 반영된 재고량 조회 (기타 입출고 포함)->직영점
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/storestocks/my")
    public ResponseEntity<List<StoreStockCheckResponseDto>> getAllStoreStocksforstore() {
        String adminLoginId = getManagerLoginId();

        log.info("가게 입고 예정량 조회: {}", adminLoginId);
        log.debug("getIncomingStock 메서드 호출됨");

        List<StoreStockCheckResponseDto> storestocks  = adminOrderService.getAllStoreStocks(adminLoginId);
        return ResponseEntity.status(HttpStatus.OK).body(storestocks);
    }

    private String getManagerLoginId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ManagerUserDetails managerUserDetails = (ManagerUserDetails)principal;

        return managerUserDetails.getUsername();
    }
    


}
