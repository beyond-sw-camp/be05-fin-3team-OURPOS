package com.ourpos.domain.menu;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴 카테고리 이름, 가격, 설명, 이미지를 수정할 수 있다.")
    @Test
    void menuUpdate() {
        // given
        MenuOption menuOption = MenuOption.builder()
            .name("옵션1")
            .price(1000)
            .build();
        MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
            .name("옵션그룹1")
            .menuOption(menuOption)
            .build();
        Category category = Category.builder()
            .name("카테고리1")
            .menuOptionGroup(menuOptionGroup)
            .build();

        Menu menu = Menu.builder()
            .category(category)
            .name("메뉴1")
            .price(10000)
            .description("맛있는 메뉴1")
            .pictureUrl("menu1.jpg")
            .build();

        Category category2 = Category.builder()
            .name("카테고리2")
            .menuOptionGroup(menuOptionGroup)
            .build();

        // when
        menu.update(category2, "메뉴2", 20000, "맛있는 메뉴2", "menu2.jpg");

        // then
        assertThat(menu)
            .extracting("category", "name", "price", "description", "pictureUrl")
            .containsExactly(category2, "메뉴2", 20000, "맛있는 메뉴2", "menu2.jpg");
    }

    @DisplayName("메뉴를 삭제하면 삭제 여부가 true가 되고 삭제시간이 설정된다.")
    @Test
    void menuDelete() {
        // given
        MenuOption menuOption = MenuOption.builder()
            .name("옵션1")
            .price(1000)
            .build();
        MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
            .name("옵션그룹1")
            .menuOption(menuOption)
            .build();
        Category category = Category.builder()
            .name("카테고리1")
            .menuOptionGroup(menuOptionGroup)
            .build();

        Menu menu = Menu.builder()
            .category(category)
            .name("메뉴1")
            .price(10000)
            .description("맛있는 메뉴1")
            .pictureUrl("menu1.jpg")
            .build();

        // when
        assertThat(menu.getDeletedYn()).isFalse();
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        menu.delete(now);

        // then
        assertThat(menu)
            .extracting("deletedYn", "deletedDateTime")
            .containsExactly(true, now);
    }

}