package com.ourpos.api.category.service;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.category.dto.response.CategoryResponseDto;
import com.ourpos.domain.menu.Category;
import com.ourpos.domain.menu.CategoryQueryRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryQueryService {

	private final CategoryQueryRepository categoryQueryRepository;

	public CategoryResponseDto findCategory(Long categoryId) {
		Category category = categoryQueryRepository.findOne(categoryId)
			.orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));

		return new CategoryResponseDto(category);
	}

	public List<CategoryResponseDto> findAllCategories() {
		List<Category> categories = categoryQueryRepository.findAllCategories();

		return categories.stream()
			.map(CategoryResponseDto::new)
			.toList();
	}
}
