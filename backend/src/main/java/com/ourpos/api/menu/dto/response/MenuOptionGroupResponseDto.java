package com.ourpos.api.menu.dto.response;

import java.util.List;

import com.ourpos.domain.menu.MenuOptionGroup;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuOptionGroupResponseDto {

    private Long menuOptionGroupId;
    private Long menuId;
    private String menuOptionGroupName;
    private String menuName;
    private List<MenuOptionResponseDto> menuOptionResponseDtos;

    public MenuOptionGroupResponseDto(MenuOptionGroup menuOptionGroup) {
        this.menuOptionGroupId = menuOptionGroup.getId();
        this.menuId = menuOptionGroup.getMenu().getId();
        this.menuOptionGroupName = menuOptionGroup.getName();
        this.menuName = menuOptionGroup.getMenu().getName();
        this.menuOptionResponseDtos = menuOptionGroup.getMenuOptions().stream()
            .map(MenuOptionResponseDto::new)
            .toList();
    }
}
