package com.ourpos.api.category.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.ourpos.domain.menu.MenuOptionGroup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuOptionGroupRequestDto {
    private String name;
    private Boolean exclusiveYn;
    private String description;
    private List<MenuOptionRequestDto> menuOptions = new ArrayList<>();

    public MenuOptionGroup toEntity() {
        return MenuOptionGroup.builder()
            .name(name)
            .exclusiveYn(exclusiveYn)
            .description(description)
            .menuOptions(menuOptions.stream()
                .map(MenuOptionRequestDto::toEntity)
                .toList())
            .build();
    }
}
