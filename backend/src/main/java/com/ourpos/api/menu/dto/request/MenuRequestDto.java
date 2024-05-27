package com.ourpos.api.menu.dto.request;

import com.ourpos.domain.menu.Category;
import com.ourpos.domain.menu.Menu;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MenuRequestDto {

    private Long categoryId;
    private String name;
    private Integer price;
    private String description;
    private String pictureUrl;

    public Menu toEntity(Category category) {
        return Menu.builder()
            .category(category)
            .name(name)
            .price(price)
            .description(description)
            .pictureUrl(pictureUrl)
            .build();
    }

}
