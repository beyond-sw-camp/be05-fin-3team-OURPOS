package com.ourpos.api.menu.dto.request;

import com.ourpos.domain.menu.Category;

public class MenuRequestDto {

	private Long storeId;
	private String name;
	private int price;
	private Category category;
	private String menuPictureUrl;

	public MenuRequestDto() {
	}

	public MenuRequestDto(String name, Long storeId, int price, MenuCategory menuCategory) {
		this.name = name;
		this.storeId = storeId;
		this.price = price;
		this.menuCategory = menuCategory;
	}
}
