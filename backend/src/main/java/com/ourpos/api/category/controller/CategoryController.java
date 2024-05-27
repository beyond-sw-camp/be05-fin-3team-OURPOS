// package com.ourpos.api.category.controller;
//
// import org.springframework.http.HttpStatus;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.ourpos.api.Result;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @RequiredArgsConstructor
// @RestController
// @RequestMapping("/api/v1/categories")
// public class CategoryController {
//
// 	private final CategoryQueryService categoryQueryService;
// 	private final CategoryServiceImpl categoryService;
//
// 	@GetMapping("/{categoryId}")
// 	public Result<CategoryResponseDto> findCategory(@PathVariable Long categoryId) {
// 		log.info("카테고리 조회: {}", categoryId);
//
// 		return new Result<>(HttpStatus.OK.value(), "메뉴 조회가 완료되었습니다.", categoryQueryService.findCategroy(categoryId));
// 	}
//
// 	@GetMapping
// 	public Result<List<CategoryResponseDto>> findAllCategories(
// 		@RequestParam
// 	)
//
// }