package com.ourpos.api.order.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuPreferResponseDto {

    private Long menuId;
    private String name;
    private String category;
    private Long quantity;

    public MenuPreferResponseDto(Long menuId, String name, String category, Long quantity) {
        this.menuId = menuId;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }
}
