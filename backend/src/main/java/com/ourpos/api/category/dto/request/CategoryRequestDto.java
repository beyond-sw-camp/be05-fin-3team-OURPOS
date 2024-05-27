package com.ourpos.api.category.dto.request;

import java.util.ArrayList;
import java.util.List;


import com.ourpos.domain.menu.Category;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryRequestDto {

	private String name;
	private List<Long> menuOptionGroupIds = new ArrayList<>();
	private List<MenuOptionGroupRequestDto> menuOptionGroups = new ArrayList<>();

	public Category toEntity() {
		return Category.builder()
			.name(name)
			.menuOptionGroups(menuOptionGroups)

			.menuOptionGroups(menuOptionGroups.stream()
				.map(MenuOptionGroupRequestDto::toEntity)
				.toList())
			.build();
	}

}
