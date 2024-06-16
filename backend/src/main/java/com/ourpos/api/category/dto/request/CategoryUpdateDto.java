package com.ourpos.api.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateDto {

	// @NotNull(message = "카테고리 아이디를 입력해 주세요")
	// @Positive(message = "1 이상의 상수만 입력해 주세요")
	// private Long categoryId;

	@NotBlank(message = "메뉴 이름을 입력해 주세요")
	private String name;
	// private List<MenuOptionGroupUpdateDto> menuOptionGroups = new ArrayList<>();

}
