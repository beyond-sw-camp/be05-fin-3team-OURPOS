package com.ourpos.api.menu.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ourpos.api.Result;
import com.ourpos.api.menu.dto.request.MenuRequestDto;
import com.ourpos.api.menu.dto.request.MenuUpdateDto;
import com.ourpos.api.menu.dto.response.MenuResponseDto;
import com.ourpos.api.menu.service.MenuQueryService;
import com.ourpos.api.menu.service.MenuServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MenuController implements MenuControllerDocs {

    private final MenuQueryService menuQueryService;
    private final MenuServiceImpl menuServiceImpl;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @GetMapping("/menus/{menuId}")
    public Result<MenuResponseDto> findMenu(@PathVariable Long menuId) {
        log.info("메뉴 조회: {}", menuId);

        MenuResponseDto menu = menuQueryService.findMenu(menuId);
        return new Result<>(HttpStatus.OK.value(), "메뉴 조회가 완료되었습니다.", menu);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @GetMapping("/menus")
    public Result<List<MenuResponseDto>> findAllMenus(
        @RequestParam(value = "category", required = false) String category) {
        log.info("MenuController.findAllMenus() called");

        List<MenuResponseDto> menus = menuQueryService.findMenusByCategory(category);
        return new Result<>(HttpStatus.OK.value(), "카테고리별 메뉴 조회가 완료되었습니다.", menus);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @PostMapping(value = "/menus", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Result<Void> addMenu(@RequestPart @Valid MenuRequestDto menuRequestDto,
        @RequestPart(required = false) MultipartFile multipartFile) {
        log.info("MenuController.addMenu() called");

        menuServiceImpl.addMenu(menuRequestDto, multipartFile);
        return new Result<>(HttpStatus.OK.value(), "메뉴 추가 성공", null);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/menus/{menuId}")
    public Result<Void> updateMenu(@PathVariable Long menuId, @RequestBody @Valid MenuUpdateDto menuUpdateDto) {
        log.info("MenuController.updateMenu() called");

        menuServiceImpl.updateMenu(menuId, menuUpdateDto);
        return new Result<>(HttpStatus.OK.value(), "메뉴 수정 성공", null);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/menus/{menuId}/delete")
    public Result<Void> deleteMenu(@PathVariable Long menuId) {
        log.info("MenuController.deleteMenu() called");
        log.info("menuId: {}", menuId);

        menuServiceImpl.deleteMenu(menuId);
        return new Result<>(HttpStatus.OK.value(), "메뉴 삭제 성공", null);
    }

}
