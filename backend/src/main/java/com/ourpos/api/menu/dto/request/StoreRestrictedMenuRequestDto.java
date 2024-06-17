package com.ourpos.api.menu.dto.request;

import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.StoreRestrictedMenu;
import com.ourpos.domain.store.Store;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StoreRestrictedMenuRequestDto {

    @NotNull(message = "메뉴 아이디를 입력해 주세요")
    @Positive(message = "1 이상의 상수만 입력해 주세요")
    private Long menuId;

    public StoreRestrictedMenu toEntity(Menu menu, Store store) {
        return StoreRestrictedMenu.builder()
            .store(store)
            .menu(menu)
            .build();
    }

}
