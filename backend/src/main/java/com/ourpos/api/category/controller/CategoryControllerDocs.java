package com.ourpos.api.category.controller;

import java.util.List;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.ourpos.api.Result;
import com.ourpos.api.category.dto.request.CategoryRequestDto;
import com.ourpos.api.category.dto.request.CategoryUpdateDto;
import com.ourpos.api.category.dto.request.MenuOptionGroupRequestDto;
import com.ourpos.api.category.dto.request.MenuOptionGroupUpdateDto;
import com.ourpos.api.category.dto.request.MenuOptionRequestDto;
import com.ourpos.api.category.dto.request.MenuOptionUpdateDto;
import com.ourpos.api.category.dto.response.CategoryResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "카테고리 API", description = "카테고리 컨트롤러 API에 대한 설명입니다.")
public interface CategoryControllerDocs {

	@Operation(summary = "개별 카테고리 조회", description = "카테고리 ID로 개별 카테고리를 조회합니다.")
	Result<CategoryResponseDto> findCategory(
		@Parameter(name = "categoryId", description = "찾으려는 카테고리의 ID") @PathVariable Long categoryId);

	@Operation(summary = "전체 카테고리 조회", description = "전체 카테고리를 조회합니다.")
	Result<List<CategoryResponseDto>> findAllCategories();

	@Operation(summary = "카테고리 추가", description = "새로운 카테고리를 추가합니다.")
	Result<Void> addCategory(
		@Parameter(name = "categoryRequestDto", description = "추가하고자 하는 카테고리의 정보")
		@RequestBody @Valid CategoryRequestDto categoryRequestDto);

	@Operation(summary = "메뉴옵션그룹 추가", description = "새로운 메뉴옵션그룹을 추가합니다.")
	Result<Void> addMenuOptionGroup(
		@Parameter(name = "menuOptionGroupRequestDto", description = "추가하고자 하는 메뉴옵션그룹의 정보")
		@RequestBody @Valid MenuOptionGroupRequestDto menuOptionGroupRequestDto);

	@Operation(summary = "메뉴옵션 추가", description = "새로운 메뉴옵션을 추가합니다.")
	Result<Void> addMenuOption(
		@Parameter(name = "menuOptionRequestDto", description = "추가하고자 하는 메뉴옵션의 정보")
		@RequestBody @Valid MenuOptionRequestDto menuOptionRequestDto);

	@Operation(summary = "카테고리 수정", description = "기존의 카테고리를 수정합니다.")
	Result<Void> updateCategory(
		@Parameter(name = "categoryId", description = "수정하고자 하는 카테고리의 ID")
		@PathVariable Long categoryId,
		@Parameter(name = "categoryUpdateDto", description = "수정하고자 하는 카테고리의 정보")
		@RequestBody @Valid CategoryUpdateDto categoryUpdateDto);

	@Operation(summary = "메뉴옵션그룹 수정", description = "기존의 메뉴옵션그룹을 수정합니다.")
	Result<Void> updateMenuOptionGroup(
		@Parameter(name = "menuOptionGroupId", description = "수정하고자 하는 메뉴옵션그룹의 ID")
		@PathVariable Long menuOptionGroupId,
		@Parameter(name = "menuOptionGroupUpdateDto", description = "수정하고자 하는 메뉴옵션그룹의 정보")
		@RequestBody @Valid MenuOptionGroupUpdateDto menuOptionGroupUpdateDto);

	@Operation(summary = "메뉴옵션 수정", description = "기존의 메뉴옵션을 수정합니다.")
	Result<Void> updateMenuOption(
		@Parameter(name = "menuOptionId", description = "수정하고자 하는 메뉴옵션의 ID")
		@PathVariable Long menuOptionId,
		@Parameter(name = "menuOptionUpdateDto", description = "수정하고자 하는 메뉴옵션의 정보")
		@RequestBody @Valid MenuOptionUpdateDto menuOptionUpdateDto);

	@Operation(summary = "카테고리 삭제", description = "기존의 카테고리를 삭제합니다.")
	Result<Void> deleteCategory(
		@Parameter(name = "categoryId", description = "삭제하고자 하는 메뉴의 ID")
		@PathVariable Long categoryId);

	@Operation(summary = "메뉴옵션그룹 삭제", description = "기존의 메뉴옵션그룹을 삭제합니다.")
	Result<Void> deleteMenuOptionGroup(
		@Parameter(name = "menuOptionGroupId", description = "삭제하고자 하는 메뉴옵션그룹의 ID")
		@PathVariable Long menuOptionGroupId);

	@Operation(summary = "메뉴옵션 삭제", description = "기존의 메뉴옵션을 삭제합니다.")
	Result<Void> deleteMenuOption(
		@Parameter(name = "menuOptionId", description = "삭제하고자 하는 메뉴옵션의 ID")
		@PathVariable Long menuOptionId);
}
