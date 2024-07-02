package com.ourpos.api.order.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.order.dto.request.KioskOrderRequestDto;
import com.ourpos.api.order.service.KioskOrderService;
import com.ourpos.auth.dto.manager.ManagerUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class KioskOrderController {

    private final KioskOrderService kioskOrderService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/orders/kiosk")
    public Result<Void> createKioskOrder(@RequestBody @Valid KioskOrderRequestDto kioskOrderRequestDto) {
        log.info("키오스크 주문");
        String loginId = getManagerLoginId();

        kioskOrderService.createKioskOrder(loginId, kioskOrderRequestDto);
        return new Result<>(HttpStatus.OK.value(), "키오스크 주문이 완료되었습니다.", null);
    }

    private String getManagerLoginId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ManagerUserDetails managerUserDetails = (ManagerUserDetails)principal;

        return managerUserDetails.getUsername();
    }
}
