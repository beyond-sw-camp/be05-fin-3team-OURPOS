package com.ourpos.api.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuOptionGroupUpdateDto {

	@NotBlank(message = "카테고리 아이디를 입력해 주세요")
	@Positive(message = "1 이상의 상수만 입력해 주세요")
	private Long categoryId;

	@NotBlank(message = "메뉴옵션그룹 이름을 입력해 주세요")
	private String name;

	@NotBlank(message = "배타선택여부(다중선택 가능 여부)를 입력해 주세요")
	private Boolean exclusiveYn;

	private String description;
	// private List<MenuOptionUpdateDto> menuOptions = new ArrayList<>();
}
