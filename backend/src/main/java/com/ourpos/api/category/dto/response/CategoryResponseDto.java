package com.ourpos.api.category.dto.response;

import java.util.List;


import com.ourpos.domain.menu.Category;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {

	private Long id;
	private String name;
	private List<MenuOptionGroupResponseDto> menuOptionGroupResponseDtos;

	public CategoryResponseDto(Category category) {
		this.id = category.getId();
		this.name = category.getName();
		this.menuOptionGroupResponseDtos = category.getMenuOptionGroups().stream()
			.map(MenuOptionGroupResponseDto::new)
			.toList();
	}
}
