package com.ourpos.api.store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.store.dto.request.StoreRequestDto;
import com.ourpos.api.store.dto.response.StoreResponseDto;
import com.ourpos.api.store.service.StoreService;
import com.ourpos.auth.dto.CustomOAuth2Customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public Result<Void> createStore(@RequestBody StoreRequestDto storeRequestDto) {
        log.info("매장 생성");
        storeService.createStore(storeRequestDto);

        return new Result<>(HttpStatus.OK.value(), "매장 생성이 완료되었습니다.", null);
    }

    @GetMapping("/hall")
    public Result<List<StoreResponseDto>> findStoresOrderByDistance(@RequestParam Double latitude,
        @RequestParam Double longitude) {
        log.info("현재 위치에서 가장 가까운 매장 정렬 조회");
        List<StoreResponseDto> stores = storeService.findStoresOrderBy(latitude, longitude);

        return new Result<>(HttpStatus.OK.value(), "매장 조회가 완료되었습니다.", stores);
    }

    @GetMapping("/delivery")
    public Result<List<StoreResponseDto>> findStoresOrderByDeliveryDistance() {
        log.info("현재 위치에서 가장 가까운 배달 매장 정렬 조회");
        String loginId = getLoginCustomerLoginId();
        List<StoreResponseDto> stores = storeService.findStoresOrderByDeliveryDistance(loginId);

        return new Result<>(HttpStatus.OK.value(), "매장 조회가 완료되었습니다.", stores);
    }

    private String getLoginCustomerLoginId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomOAuth2Customer customOAuth2Customer = (CustomOAuth2Customer)principal;

        return customOAuth2Customer.getLoginId();
    }
}
