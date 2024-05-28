package com.ourpos.api.category.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuOptionUpdateDto {
	private String name;
	private Integer price;
}
