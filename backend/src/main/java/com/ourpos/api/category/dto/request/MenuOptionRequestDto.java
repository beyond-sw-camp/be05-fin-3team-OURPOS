package com.ourpos.api.category.dto.request;

import com.ourpos.domain.menu.MenuOption;
import com.ourpos.domain.menu.MenuOptionGroup;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class MenuOptionRequestDto {
	private String name;
	private Integer price;
	private Long menuOptionGroupId;

	public MenuOption toEntity(MenuOptionGroup menuOptionGroup) {
		return MenuOption.builder()
			.name(name)
			.price(price)
			.menuOptionGroup(menuOptionGroup)
			.build();
	}
}
