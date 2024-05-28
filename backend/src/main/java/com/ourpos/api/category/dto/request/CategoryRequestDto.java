package com.ourpos.api.category.dto.request;

import com.ourpos.domain.menu.Category;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryRequestDto {

	private String name;
	// private List<MenuOptionGroupRequestDto> menuOptionGroups = new ArrayList<>();

	public Category toEntity() {

		return Category.builder()
			.name(name)
			// .menuOptionGroups(menuOptionGroups.stream()
			// 	.map(MenuOptionGroupRequestDto::toEntity)
			// 	.toList())
			.build();
	}

}
