package com.ourpos.api.menu.dto.request;

import java.util.ArrayList;
import java.util.List;


import com.ourpos.domain.menu.Category;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.store.Store;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MenuRequestDto {

	private Long storeId;
	private Long categoryId;
	private String name;
	private Integer price;
	private Boolean availableYn;
	private String description;
	private String pictureUrl;
	private List<MenuOptionGroupRequestDto> menuOptionGroups = new ArrayList<>();

	public Menu toEntity(Store store, Category category) {
		return Menu.builder()
			.store(store)
			.category(category)
			.name(name)
			.price(price)
			.availableYn(availableYn)
			.description(description)
			.pictureUrl(pictureUrl)
			.menuOptionGroups(menuOptionGroups.stream()
				.map(MenuOptionGroupRequestDto::toEntity)
				.toList())
			.build();
	}

}
