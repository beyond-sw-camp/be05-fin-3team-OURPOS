package com.ourpos.api.category.service;

import org.springframework.stereotype.Service;

import com.ourpos.api.category.dto.request.CategoryRequestDto;

@Service
public interface CategoryService {

	public void addCategory(CategoryRequestDto categoryRequestDto);

	public void deleteCategory(Long categoryId);

	public void updateCategory(Long categoryId, CategoryUpdateDto categoryUpdateDto);

}
