package com.ourpos.api.category.dto.request;

import com.ourpos.domain.menu.MenuOption;
import com.ourpos.domain.menu.MenuOptionGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class MenuOptionRequestDto {

	@NotBlank(message = "메뉴옵션그룹 아이디를 입력해 주세요")
	@Positive(message = "1 이상의 상수만 입력해 주세요")
	private Long menuOptionGroupId;

	@NotBlank(message = "메뉴옵션 이름을 입력해 주세요")
	private String name;

	@NotBlank(message = "메뉴옵션 가격을 입력해 주세요")
	@PositiveOrZero(message = "메뉴옵션 가격은 0이상의 숫자만 가능합니다")
	private Integer price;

	public MenuOption toEntity(MenuOptionGroup menuOptionGroup) {
		return MenuOption.builder()
			.name(name)
			.price(price)
			.menuOptionGroup(menuOptionGroup)
			.build();
	}
}
