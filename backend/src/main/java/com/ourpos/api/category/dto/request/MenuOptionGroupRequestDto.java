package com.ourpos.api.category.dto.request;

import com.ourpos.domain.menu.Category;
import com.ourpos.domain.menu.MenuOptionGroup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuOptionGroupRequestDto {
	private String name;
	private Boolean exclusiveYn;
	private String description;
	private Long categoryId;
	// private List<MenuOptionRequestDto> menuOptions = new ArrayList<>();

	public MenuOptionGroup toEntity(Category category) {
		return MenuOptionGroup.builder()
			.name(name)
			.exclusiveYn(exclusiveYn)
			.description(description)
			.category(category)
			// .menuOptions(menuOptions.stream()
			// 	.map(MenuOptionRequestDto::toEntity)
			// 	.toList())
			.build();
	}
}
