package com.ourpos.api.category.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateDto {

	private Long categoryId;
	private String categoryName;
	// private List<MenuOptionGroupUpdateDto> menuOptionGroups = new ArrayList<>();

}
