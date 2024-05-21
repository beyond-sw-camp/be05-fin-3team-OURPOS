package com.ourpos.api.menu.dto.request;

public class MenuRequestDto {

	private Long storeId;
	private Long categoryId;
	private String menuName;
	private Integer menuPrice;

	private String menuPictureUrl;

	public MenuRequestDto() {
	}

	// public MenuRequestDto(String name, Long storeId, int price, MenuCategory menuCategory) {
	// 	this.name = name;
	// 	this.storeId = storeId;
	// 	this.price = price;
	// 	this.menuCategory = menuCategory;
	// }
}
