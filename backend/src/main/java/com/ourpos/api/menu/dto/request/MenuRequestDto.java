package com.ourpos.api.menu.dto.request;

import com.ourpos.domain.menu.Category;
import com.ourpos.domain.menu.Menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MenuRequestDto {

	@NotBlank(message = "카테고리 아이디를 입력해 주세요")
	@Positive(message = "1 이상의 상수만 입력해 주세요")
	private Long categoryId;

	@NotBlank(message = "메뉴 이름을 입력해 주세요")
	private String name;

	@NotBlank(message = "메뉴 가격을 입력해 주세요")
	@PositiveOrZero(message = "메뉴 가격은 0이상의 숫자만 가능합니다")
	private Integer price;

	private String description;

	private String pictureUrl;

	public Menu toEntity(Category category) {
		return Menu.builder()
			.category(category)
			.name(name)
			.price(price)
			.description(description)
			.pictureUrl(pictureUrl)
			.build();
	}

}
