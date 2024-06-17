package com.ourpos.api.menu.controller;

import java.util.List;


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
import com.ourpos.api.menu.dto.request.StoreRestrictedMenuRequestDto;
import com.ourpos.api.menu.dto.response.MenuResponseDto;
import com.ourpos.api.menu.service.MenuQueryService;
import com.ourpos.api.menu.service.MenuServiceImpl;

import jakarta.validation.Valid;
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

    // 메뉴 활성화 정보 포함하여 응답 (손님용)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @GetMapping("/menus")
    public Result<List<MenuResponseDto>> findAllMenus(
        @RequestParam(value = "category", required = false) String category, Long storeId) {
        log.info("MenuController.findAllMenus() called");
        List<MenuResponseDto> menus = menuQueryService.findMenusByCategory(category, storeId);
        return new Result<>(HttpStatus.OK.value(), "카테고리별 메뉴 조회가 완료되었습니다.", menus);
    }

    // 메뉴 활성화 정보 제외하여 응답(본사용)
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("/menus/all")
    public Result<List<MenuResponseDto>> findAllMenusAdmin(
        @RequestParam(value = "category", required = false) String category) {
        log.info("MenuController.findAllMenusAdmin() called");
        List<MenuResponseDto> menus = menuQueryService.findMenusByCategoryAdmin(category);
        return new Result<>(HttpStatus.OK.value(), "점주,관리자용 카테고리별 메뉴 조회가 완료되었습니다.", menus);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/menus/deactivate")
    public Result<Void> deactivateMenu(
        @RequestBody @Valid StoreRestrictedMenuRequestDto storeRestrictedMenuRequestDto) {
        log.info("MenuController.deactivateMenu() called with  menuId: {}",
            storeRestrictedMenuRequestDto.getMenuId());
        menuServiceImpl.deactivateMenu(storeRestrictedMenuRequestDto);
        return new Result<>(HttpStatus.OK.value(), "메뉴 비활성화 성공", null);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/menus/activate")
    public Result<Void> activateMenu(
        @RequestBody @Valid StoreRestrictedMenuRequestDto storeRestrictedMenuRequestDto) {
        log.info("MenuController.activateMenu() called with and menuId: {}",
            storeRestrictedMenuRequestDto.getMenuId());
        menuServiceImpl.activateMenu(storeRestrictedMenuRequestDto);
        return new Result<>(HttpStatus.OK.value(), "메뉴 활성화 성공", null);
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
    @PostMapping(value = "/menus/{menuId}")
    public Result<Void> updateMenu(@PathVariable Long menuId, @RequestPart @Valid MenuUpdateDto menuUpdateDto,
        @RequestPart MultipartFile multipartFile) {
        log.info("MenuController.updateMenu() called");

        menuServiceImpl.updateMenu(menuId, menuUpdateDto, multipartFile);
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
