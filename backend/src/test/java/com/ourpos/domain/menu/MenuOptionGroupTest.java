package com.ourpos.domain.menu;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuOptionGroupTest {

    @DisplayName("메뉴 옵션 그룹의 카테고리, 이름, 베타선택여부, 설명을 수정할 수 있다.")
    @Test
    void test() {
        // given
        Category category1 = Category.builder()
            .name("치킨")
            .build();

        Category category2 = Category.builder()
            .name("피자")
            .build();

        MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
            .name("소스")
            .exclusiveYn(true)
            .description("맛있는 소스")
            .build();
        category1.addMenuOptionGroup(menuOptionGroup);

        // when
        assertThat(menuOptionGroup).extracting("category", "name", "exclusiveYn", "description")
            .containsExactly(category1, "소스", true, "맛있는 소스");
        menuOptionGroup.update(category2, "토핑", false, "맛있는 토핑");

        // then
        assertThat(menuOptionGroup).extracting("category", "name", "exclusiveYn", "description")
            .containsExactly(category2, "토핑", false, "맛있는 토핑");
    }

    @DisplayName("메뉴 옵션 그룹을 삭제 처리할 수 있다. 메뉴 옵션 그룹을 삭제하면 메뉴 옵션 그룹도 삭제처리 된다.")
    @Test
    void deleteMenuOptionGroup() {
        // given
        Category category = Category.builder()
            .name("치킨")
            .build();

        MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
            .name("소스")
            .exclusiveYn(true)
            .description("맛있는 소스")
            .build();
        category.addMenuOptionGroup(menuOptionGroup);

        // when
        assertThat(menuOptionGroup.getDeletedYn()).isFalse();
        assertThat(menuOptionGroup.getDeletedDateTime()).isNull();

        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        menuOptionGroup.delete(now);

        // then
        assertThat(menuOptionGroup.getDeletedYn()).isTrue();
        assertThat(menuOptionGroup.getDeletedDateTime()).isEqualTo(now);
    }

}