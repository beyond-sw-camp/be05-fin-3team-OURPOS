package com.ourpos.api.menu.dto.response;

import com.ourpos.domain.menu.Menu;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuResponseDto {

	private Long Id;
	private Long categoryId;
	private String name;
	private Integer price;
	private String description;
	private String pictureUrl;

	public MenuResponseDto(Menu menu) {
		this.Id = menu.getId();
		this.categoryId = menu.getCategory().getId();
		this.name = menu.getName();
		this.price = menu.getPrice();
		this.description = menu.getDescription();
		this.pictureUrl = menu.getPictureUrl();
	}
}
