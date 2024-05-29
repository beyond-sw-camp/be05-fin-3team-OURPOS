package com.ourpos.api.category.dto.request;

import com.ourpos.domain.menu.Category;
import com.ourpos.domain.menu.MenuOptionGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuOptionGroupRequestDto {

	@NotNull(message = "카테고리 아이디를 입력해 주세요")
	@Positive(message = "1 이상의 상수만 입력해 주세요")
	private Long categoryId;

	@NotBlank(message = "메뉴옵션그룹 이름을 입력해 주세요")
	private String name;

	@NotBlank(message = "배타선택여부(다중선택 가능 여부)를 입력해 주세요")
	private Boolean exclusiveYn;

	private String description;

	// private List<MenuOptionRequestDto> menuOptions = new ArrayList<>();

	public MenuOptionGroup toEntity(Category category) {
		return MenuOptionGroup.builder()
			.name(name)
			.exclusiveYn(exclusiveYn)
			.description(description)
			.category(category)
			// .menuOptions(menuOptions.stream()
			// 	.map(MenuOptionRequestDto::toEntity)
			// 	.toList())
			.build();
	}
}
