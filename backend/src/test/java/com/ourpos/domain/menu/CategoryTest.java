package com.ourpos.domain.menu;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @DisplayName("카테고리의 이름을 수정할 수 있다.")
    @Test
    void updateCategory() {
        // given
        Category category = Category.builder()
            .name("치킨")
            .build();

        // when
        assertThat(category.getName()).isEqualTo("치킨");
        category.update("피자");

        // then
        assertThat(category.getName()).isEqualTo("피자");
    }

    @DisplayName("카테고리를 삭제 처리할 수 있다.")
    @Test
    void deleteCategory() {
        // given
        Category category = Category.builder()
            .name("치킨")
            .build();

        // when
        assertThat(category.getDeletedYn()).isFalse();

        LocalDateTime now = LocalDateTime.now();
        category.delete(now);

        // then
        assertThat(category.getDeletedYn()).isTrue();
        assertThat(category.getDeletedDateTime()).isEqualTo(now);
    }

    @DisplayName("카테고리에 메뉴 옵션 그룹을 추가할 수 있다.")
    @Test
    void addMenuOptionGroup() {
        // given
        Category category = Category.builder()
            .name("치킨")
            .build();
        MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
            .name("소스")
            .build();

        // when
        category.addMenuOptionGroup(menuOptionGroup);

        // then
        assertThat(category.getMenuOptionGroups()).contains(menuOptionGroup);
        assertThat(menuOptionGroup.getCategory()).isEqualTo(category);
    }

    @DisplayName("카테고리를 삭제 처리하게 되면 해당 카테고리에 속한 메뉴 옵션 그룹도 삭제 처리된다.")
    @Test
    void deleteMenuOptionGroup() {
        // given
        Category category = Category.builder()
            .name("치킨")
            .build();
        MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
            .name("소스")
            .build();
        category.addMenuOptionGroup(menuOptionGroup);

        // when
        assertThat(menuOptionGroup.getDeletedYn()).isFalse();
        LocalDateTime now = LocalDateTime.now();
        category.delete(now);

        // then
        assertThat(menuOptionGroup.getDeletedYn()).isTrue();
        assertThat(menuOptionGroup.getDeletedDateTime()).isEqualTo(now);
        category.getMenuOptionGroups().forEach(mog -> {
            assertThat(mog.getDeletedYn()).isTrue();
            assertThat(mog.getDeletedDateTime()).isEqualTo(now);
        });
    }

}