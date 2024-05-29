package com.ourpos.api.category.dto.request;

import com.ourpos.domain.menu.Category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryRequestDto {

	@NotBlank(message = "카테고리 이름을 입력해 주세요")
	private String name;
	// private List<MenuOptionGroupRequestDto> menuOptionGroups = new ArrayList<>();

	public Category toEntity() {

		return Category.builder()
			.name(name)
			// .menuOptionGroups(menuOptionGroups.stream()
			// 	.map(MenuOptionGroupRequestDto::toEntity)
			// 	.toList())
			.build();
	}

}
