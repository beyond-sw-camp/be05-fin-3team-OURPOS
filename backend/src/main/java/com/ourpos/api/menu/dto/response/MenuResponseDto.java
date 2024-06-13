package com.ourpos.api.menu.dto.response;

import com.ourpos.domain.menu.Menu;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuResponseDto {

    private Long Id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private Integer price;
    private String description;
    private String pictureUrl;
    private Boolean available;

    public MenuResponseDto(Menu menu) {
        this.Id = menu.getId();
        this.categoryId = menu.getCategory().getId();
        this.categoryName = menu.getCategory().getName();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.description = menu.getDescription();
        this.pictureUrl = menu.getPictureUrl();
    }
}
