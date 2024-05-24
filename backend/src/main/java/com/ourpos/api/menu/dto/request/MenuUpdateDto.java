package com.ourpos.api.menu.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuUpdateDto {

	private Long categoryId;
	private String name;
	private Integer price;
	private String description;
	private String pictureUrl;
	// private List<MenuOptionGroupUpdateDto> menuOptionGroups = new ArrayList<>();

}
