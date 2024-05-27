package com.ourpos.api.category.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuOptionGroupUpdateDto {
	private Long Id;
	private Long categoryId;
	private String name;
	private Boolean exclusiveYn;
	private String description;
	// private List<MenuOptionUpdateDto> menuOptions = new ArrayList<>();
}
