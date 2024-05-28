package com.ourpos.api.category.dto.response;

import java.util.List;


import com.ourpos.domain.menu.MenuOptionGroup;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuOptionGroupResponseDto {

	private Long Id;
	private Long categoryId;
	private String name;
	private List<MenuOptionResponseDto> menuOptionResponseDtos;

	public MenuOptionGroupResponseDto(MenuOptionGroup menuOptionGroup) {
		this.Id = menuOptionGroup.getId();
		this.name = menuOptionGroup.getName();
		this.categoryId = menuOptionGroup.getCategory().getId();
		this.menuOptionResponseDtos = menuOptionGroup.getMenuOptions().stream()
			.map(MenuOptionResponseDto::new)
			.toList();
	}
}
