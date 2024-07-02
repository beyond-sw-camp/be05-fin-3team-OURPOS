package com.ourpos.api.store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.store.Location;
import com.ourpos.api.store.dto.request.StoreRequestDto;
import com.ourpos.api.store.dto.response.StoreResponseDto;
import com.ourpos.api.store.service.StoreService;
import com.ourpos.auth.dto.customer.CustomOAuth2Customer;
import com.ourpos.auth.exception.LoginRequiredException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public Result<Void> createStore(@RequestBody StoreRequestDto storeRequestDto) {
        log.info("매장 생성");
        storeService.createStore(storeRequestDto);

        return new Result<>(HttpStatus.OK.value(), "매장 생성이 완료되었습니다.", null);
    }

    @GetMapping("/hall")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Result<List<StoreResponseDto>> findStoresOrderByDistance(@RequestParam Double latitude,
        @RequestParam Double longitude) {
        log.info("현재 위치에서 가장 가까운 매장 정렬 조회");
        List<StoreResponseDto> stores = storeService.findStoresOrderBy(new Location(latitude, longitude));

        return new Result<>(HttpStatus.OK.value(), "매장 조회가 완료되었습니다.", stores);
    }

    @GetMapping("/delivery")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Result<List<StoreResponseDto>> findStoresOrderByDeliveryDistance() {
        log.info("나의 주소에서 가까운 배달 매장 정렬 조회");
        String loginId = getCustomerLoginId();
        List<StoreResponseDto> stores = storeService.findStoresOrderByDeliveryDistance(loginId);

        return new Result<>(HttpStatus.OK.value(), "매장 조회가 완료되었습니다.", stores);
    }

    private String getCustomerLoginId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof CustomOAuth2Customer)) {
            throw new LoginRequiredException("로그인이 필요합니다.");
        }

        return ((CustomOAuth2Customer)principal).getLoginId();
    }
}
