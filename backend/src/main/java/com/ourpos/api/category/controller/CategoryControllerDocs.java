package com.ourpos.api.category.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.ourpos.api.Result;
import com.ourpos.api.category.dto.response.CategoryResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "카테고리", description = "카테고리 관련 API")
public interface CategoryControllerDocs {

    @Operation(summary = "카테고리 조회", description = "카테고리를 조회합니다.")
    Result<CategoryResponseDto> findCategory(
        @Parameter(description = "카테고리 ID") @PathVariable Long categoryId);

    @Operation(summary = "전체 카테고리 조회", description = "전체 카테고리를 조회합니다.")
    Result<List<CategoryResponseDto>> findAllCategories();
}
