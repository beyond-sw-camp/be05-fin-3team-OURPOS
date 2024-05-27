package com.ourpos.api.menu.dto.request;

import com.ourpos.domain.menu.MenuOption;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class MenuOptionRequestDto {
    private String name;
    private Integer price;

    public MenuOption toEntity() {
        return MenuOption.builder()
            .name(name)
            .price(price)
            .build();
    }
}
