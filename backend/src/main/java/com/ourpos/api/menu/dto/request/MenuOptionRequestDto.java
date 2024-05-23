package com.ourpos.api.menu.dto.request;

import com.ourpos.domain.menu.MenuOption;
import com.ourpos.domain.menu.MenuOptionGroup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuOptionRequestDto {
	private Long menuOptionGroupId;
	private String name;
	private String price;

	public MenuOption toEntity(MenuOptionGroup menuOptionGroup) {
		return MenuOption.builder()
			.menuOptionGroup(menuOptionGroup)
			.name(name)
			.build();
	}
}
