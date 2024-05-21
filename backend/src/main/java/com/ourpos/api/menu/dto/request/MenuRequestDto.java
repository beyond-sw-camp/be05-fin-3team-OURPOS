package com.ourpos.api.menu.dto.request;

import java.time.LocalDateTime;

import com.ourpos.domain.menu.Category;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.store.Store;

public class MenuRequestDto {

	private Long storeId;
	private Long categoryId;
	private String menuName;
	private Integer menuPrice;
	private Boolean menuAvailableYn;
	private String menuDescription;
	private String menuPictureUrl;
	private LocalDateTime deletedDateTime;

	public Menu toEntity(Store store, Category category) {
		return Menu.builder()
			.store(store)
			.category(category)
			.name(menuName)
			.price(menuPrice)
			.availableYn(menuAvailableYn)
			.description(menuDescription)
			.pictureUrl(menuPictureUrl)
			.build();
	}

}
