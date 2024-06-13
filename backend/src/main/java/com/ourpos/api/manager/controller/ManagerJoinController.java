package com.ourpos.api.manager.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.manager.dto.request.ManagerJoinRequestDto;
import com.ourpos.api.manager.service.ManagerJoinService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/managers")
public class ManagerJoinController {

    private final ManagerJoinService managerJoinService;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/join")
    public Result<Void> join(@Valid @RequestBody ManagerJoinRequestDto requestDto) {
        managerJoinService.join(requestDto);

        return new Result<>(HttpStatus.OK.value(), "관리자 회원가입 성공", null);
    }
}
