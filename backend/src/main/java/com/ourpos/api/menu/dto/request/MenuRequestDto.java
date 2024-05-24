package com.ourpos.api.menu.dto.request;

import java.util.ArrayList;
import java.util.List;


import com.ourpos.domain.menu.Category;
import com.ourpos.domain.menu.Menu;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MenuRequestDto {

	private Long categoryId;
	private String name;
	private Integer price;
	private String description;
	private String pictureUrl;
	private List<MenuOptionGroupRequestDto> menuOptionGroups = new ArrayList<>();

	public Menu toEntity(Category category) {
		return Menu.builder()
			.category(category)
			.name(name)
			.price(price)
			.description(description)
			.pictureUrl(pictureUrl)
			.menuOptionGroups(menuOptionGroups.stream()
				.map(MenuOptionGroupRequestDto::toEntity)
				.toList())
			.build();
	}

}
