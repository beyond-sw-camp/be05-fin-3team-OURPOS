package com.ourpos.api.category.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuOptionUpdateDto {
	private Long menuOptionGroupId;
	private String name;
	private Integer price;
}