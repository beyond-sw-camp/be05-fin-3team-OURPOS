package com.ourpos.api.category.controller;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.category.dto.request.CategoryRequestDto;
import com.ourpos.api.category.dto.request.MenuOptionGroupUpdateDto;
import com.ourpos.api.category.dto.request.MenuOptionUpdateDto;
import com.ourpos.api.category.dto.response.CategoryResponseDto;
import com.ourpos.api.category.service.CategoryQueryService;
import com.ourpos.api.category.service.CategoryServiceImpl;
import com.ourpos.api.category.service.CategoryUpdateDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

	private final CategoryQueryService categoryQueryService;
	private final CategoryServiceImpl categoryServiceImpl;

	@GetMapping("/{categoryId}")
	public Result<CategoryResponseDto> findCategory(@PathVariable Long categoryId) {
		System.out.println("개별 카테고리 조회 컨트롤러 가동");
		log.info("카테고리 조회: {}", categoryId);

		return new Result<>(HttpStatus.OK.value(), "카테고리 조회가 완료되었습니다.", categoryQueryService.findCategory(categoryId));
	}

	@GetMapping
	public Result<List<CategoryResponseDto>> findAllCategories() {
		System.out.println("전체 카테고리 조회 컨트롤러 가동");
		log.info("전체 카테고리 조회");
		List<CategoryResponseDto> categories = categoryQueryService.findAllCategories();
		System.out.println("카테고리쿼리서비스로부터 회신 받음");
		return new Result<>(HttpStatus.OK.value(), "전체 카테고리 조회완료.", categoryQueryService.findAllCategories());
	}

	@PostMapping
	public Result<Void> addCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
		log.info("CategoryController.addCategory() called");

		categoryServiceImpl.addCategory(categoryRequestDto);
		return new Result<>(HttpStatus.OK.value(), "카테고리 추가 완료", null);
	}

	@PutMapping("/{categoryId}")
	public Result<Void> updateCategory(@PathVariable Long categoryId,
		@RequestBody CategoryUpdateDto categoryUpdateDto) {
		log.info("CategoryController.updateCategory() called");

		categoryServiceImpl.updateCategory(categoryId, categoryUpdateDto);
		return new Result<>(HttpStatus.OK.value(), "카테고리 수정 완료", null);
	}

	@PutMapping("/menuOptionGroups/{menuOptionGroupId}")
	public Result<Void> updateMenuOptionGroup(@PathVariable Long menuOptionGroupId,
		@RequestBody MenuOptionGroupUpdateDto menuOptionGroupUpdateDto) {
		log.info("CategoryController.updateMenuOptionGroup() called");
		categoryServiceImpl.updateMenuOptionGroup(menuOptionGroupId, menuOptionGroupUpdateDto);
		return new Result<>(HttpStatus.OK.value(), "메뉴 옵션 그룹 수정 성공", null);
	}

	@PutMapping("/menuOptions/{menuOptionId}")
	public Result<Void> updateMenuOption(@PathVariable Long menuOptionId,
		@RequestBody MenuOptionUpdateDto menuOptionUpdateDto) {
		log.info("CategoryController.updateMenuOption() called");
		categoryServiceImpl.updateMenuOption(menuOptionId, menuOptionUpdateDto);
		return new Result<>(HttpStatus.OK.value(), "메뉴 옵션 그룹 수정 성공", null);
	}

	@DeleteMapping("/{categoryId}")
	public Result<Void> deleteCategory(@PathVariable Long categoryId) {
		log.info("CategoryController.deleteCategory() called");
		log.info("categoryId: {}", categoryId);
		categoryServiceImpl.deleteCategory(categoryId);
		return new Result<>(HttpStatus.OK.value(), "카테고리 삭제 성공", null);
	}
}
