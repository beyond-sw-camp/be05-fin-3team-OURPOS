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
	private Boolean exclusiveYn;
	private String description;
	private List<MenuOptionResponseDto> menuOptionResponseDtos;

	public MenuOptionGroupResponseDto(MenuOptionGroup menuOptionGroup) {
		this.Id = menuOptionGroup.getId();
		this.categoryId = menuOptionGroup.getCategory().getId();
		this.name = menuOptionGroup.getName();
		this.exclusiveYn = menuOptionGroup.getExclusiveYn();
		this.description = menuOptionGroup.getDescription();
		this.menuOptionResponseDtos = menuOptionGroup.getMenuOptions().stream()
			.map(MenuOptionResponseDto::new)
			.toList();
	}
}
