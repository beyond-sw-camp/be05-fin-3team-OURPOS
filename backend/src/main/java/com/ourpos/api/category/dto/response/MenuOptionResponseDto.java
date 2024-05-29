package com.ourpos.api.category.dto.response;

import com.ourpos.domain.menu.MenuOption;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuOptionResponseDto {

	private Long menuOptionId;
	private Long menuOptionGroupId;
	private String menuOptionName;
	private Integer price;

	public MenuOptionResponseDto(MenuOption menuOption) {
		this.menuOptionId = menuOption.getId();
		this.menuOptionGroupId = menuOption.getMenuOptionGroup().getId();
		this.menuOptionName = menuOption.getName();
		this.price = menuOption.getPrice();
	}
}
