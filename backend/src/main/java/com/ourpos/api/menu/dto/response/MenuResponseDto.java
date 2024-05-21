package com.ourpos.api.menu.dto.response;

import com.ourpos.domain.menu.Menu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuResponseDto {

    private Long menuId;
    private String menuName;
    private Integer price;
    private String description;
    private String pictureUrl;
    private String categoryName;

    public MenuResponseDto(Menu menu) {
        this.menuId = menu.getId();
        this.menuName = menu.getName();
        this.price = menu.getPrice();
        this.description = menu.getDescription();
        this.pictureUrl = menu.getPictureUrl();
        this.categoryName = menu.getCategory().getName();
    }
}
