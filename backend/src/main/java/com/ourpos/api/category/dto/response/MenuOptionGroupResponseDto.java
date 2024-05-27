package com.ourpos.api.category.dto.response;

import java.util.List;


import com.ourpos.domain.menu.MenuOptionGroup;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuOptionGroupResponseDto {

	private Long menuOptionGroupId;
	private String menuOptionGroupName;
	// private Long categoryId;
	// private String categoryName;
	private List<MenuOptionResponseDto> menuOptionResponseDtos;

	public MenuOptionGroupResponseDto(MenuOptionGroup menuOptionGroup) {
		this.menuOptionGroupId = menuOptionGroup.getId();
		this.menuOptionGroupName = menuOptionGroup.getName();
		// this.categoryId = menuOptionGroup.getCategory().getId();
		// this.categoryName = menuOptionGroup.getCategory().getName();
		this.menuOptionResponseDtos = menuOptionGroup.getMenuOptions().stream()
			.map(MenuOptionResponseDto::new)
			.toList();
	}
}
