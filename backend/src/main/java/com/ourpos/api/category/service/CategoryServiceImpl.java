package com.ourpos.api.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.category.dto.request.CategoryRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
	public static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";

	@Override
	public void addCategory(CategoryRequestDto categoryRequestDto) {

	}

	@Override
	public void deleteCategory(Long categoryId) {

	}

	@Override
	public void updateCategory(Long categoryId, CategoryUpdateDto categoryUpdateDto) {

	}
}
