package com.ourpos.api.menu.dto.request;

import com.ourpos.domain.menu.MenuOptionGroup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuOptionGroupRequestDto {
	private Long menuId;
	private String name;
	private Boolean exclusiveYn;
	private String description;

	public MenuOptionGroup toEntity() {
		return MenuOptionGroup.builder()
			.name(name)
			.exclusiveYn(exclusiveYn)
			.description(description)
			.build();
	}
}
