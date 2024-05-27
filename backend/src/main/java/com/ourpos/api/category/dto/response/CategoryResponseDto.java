package com.ourpos.api.category.dto.response;

import java.util.List;


import com.ourpos.domain.menu.Category;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {

	private Long categoryId;
	private String categoryName;
	private List<MenuOptionGroupResponseDto> menuOptionGroupResponseDtos;

	public CategoryResponseDto(Category category) {
		this.categoryId = category.getId();
		this.categoryName = category.getName();
		this.menuOptionGroupResponseDtos = category.getMenuOptionGroups().stream()
			.map(MenuOptionGroupResponseDto::new)
			.toList();
	}
}
