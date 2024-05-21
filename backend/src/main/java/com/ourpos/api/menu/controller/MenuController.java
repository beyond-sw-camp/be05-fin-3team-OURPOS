package com.ourpos.api.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.menu.dto.response.MenuResponseDto;
import com.ourpos.api.menu.service.MenuQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/menus")
public class MenuController {

    private final MenuQueryService menuQueryService;

    @GetMapping("/{menuId}")
    public Result<MenuResponseDto> findMenu(@PathVariable Long menuId) {
        log.info("메뉴 조회: {}", menuId);

        return new Result<>(HttpStatus.OK.value(), "메뉴 조회가 완료되었습니다.", menuQueryService.findMenu(menuId));
    }
}
