package com.ourpos.api.menu.controller;

import java.util.List;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ourpos.api.Result;
import com.ourpos.api.menu.dto.request.MenuRequestDto;
import com.ourpos.api.menu.dto.request.MenuUpdateDto;
import com.ourpos.api.menu.dto.response.MenuResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "메뉴 API", description = "메뉴 컨트롤러 API에 대한 설명입니다.")
public interface MenuControllerDocs {

    @Operation(summary = "개별 메뉴 조회", description = "메뉴 ID로 개별 메뉴를 조회합니다.")
    Result<MenuResponseDto> findMenu(
        @Parameter(name = "menuId", description = "찾으려는 메뉴의 ID") @PathVariable Long categoryId);

    @Operation(summary = "카테고리 별 전체 메뉴 조회", description = "고객용으로 카테고리 별 전체 메뉴를 조회합니다.")
    Result<List<MenuResponseDto>> findAllMenus(
        @Parameter(name = "category", description = "조회하고자 하는 카테고리의 이름")
        @RequestParam(value = "category", required = false) String category,
        @Parameter(name = "storeId", description = "조회하고자 하는 가게의 아이디")
        @RequestParam(value = "storeId", required = false) Long storeId);

    @Operation(summary = "점주, 관리자용 카테고리 별 전체 메뉴 조회", description = "점주와 관리자용으로 카테고리 별 전체 메뉴를 조회합니다.")
    Result<List<MenuResponseDto>> findAllMenusAdmin(
        @Parameter(name = "category", description = "조회하고자 하는 카테고리의 이름")
        @RequestParam(value = "category", required = false) String category);

    @Operation(summary = "메뉴 추가", description = "새로운 메뉴를 추가합니다.")
    Result<Void> addMenu(
        @Parameter(name = "menuRequestDto", description = "추가하고자 하는 메뉴의 정보")
        @RequestPart @Valid MenuRequestDto menuRequestDto,
        @Parameter(name = "multipartFile", description = "추가하고자 하는 메뉴의 사진")
        @RequestPart(required = false) MultipartFile multipartFile);

    @Operation(summary = "메뉴 수정", description = "기존의 메뉴를 수정합니다.")
    Result<Void> updateMenu(
        @Parameter(name = "menuId", description = "수정하고자 하는 메뉴의 ID")
        @PathVariable Long menuId,
        @Parameter(name = "menuUpdateDto", description = "수정하고자 하는 메뉴의 정보")
        @RequestBody @Valid MenuUpdateDto menuUpdateDto,
        @Parameter(name = "multipartFile", description = "수정하고자 하는 메뉴의 사진")
        @RequestPart(required = false) MultipartFile multipartFile);

    @Operation(summary = "메뉴 삭제", description = "기존의 메뉴를 삭제합니다.")
    Result<Void> deleteMenu(
        @Parameter(name = "menuId", description = "삭제하고자 하는 메뉴의 ID")
        @PathVariable Long menuId);

}
