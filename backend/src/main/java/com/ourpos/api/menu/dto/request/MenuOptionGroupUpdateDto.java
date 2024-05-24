package com.ourpos.api.menu.dto.request;

import java.util.ArrayList;
import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuOptionGroupUpdateDto {
	private Long menuId;
	private String name;
	private Boolean exclusiveYn;
	private String description;
	private List<MenuOptionUpdateDto> menuOptions = new ArrayList<>();
}
