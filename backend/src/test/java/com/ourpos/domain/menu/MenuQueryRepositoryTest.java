package com.ourpos.domain.menu;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class MenuQueryRepositoryTest {

    @Autowired
    private MenuQueryRepository menuQueryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("카테고리 이름으로 삭제 처리 되지 않은 메뉴를 조회할 수 있다.")
    @Test
    void findAllWithCategory() {
        // given
        Category category = Category.builder()
            .name("치킨")
            .build();
        categoryRepository.save(category);

        Menu menu1 = Menu.builder()
            .category(category)
            .name("후라이드치킨")
            .price(16000)
            .description("맛있는 후라이드치킨")
            .pictureUrl("fried-chicken.jpg")
            .build();

        Menu menu2 = Menu.builder()
            .category(category)
            .name("양념치킨")
            .price(17000)
            .description("맛있는 양념치킨")
            .pictureUrl("seasoned-chicken.jpg")
            .build();
        menuRepository.saveAll(List.of(menu1, menu2));

        // when
        List<Menu> menus = menuQueryRepository.findAllWithCategory("치킨");

        // then
        assertThat(menus).extracting("name", "price", "description", "pictureUrl")
            .containsExactlyInAnyOrder(
                tuple("후라이드치킨", 16000, "맛있는 후라이드치킨", "fried-chicken.jpg"),
                tuple("양념치킨", 17000, "맛있는 양념치킨", "seasoned-chicken.jpg")
            );
    }

    @DisplayName("메뉴 ID로 삭제 처리 되지 않은 메뉴를 조회할 수 있다.")
    @Test
    void findOne() {
        // given
        Category category = Category.builder()
            .name("치킨")
            .build();
        categoryRepository.save(category);

        Menu menu1 = Menu.builder()
            .category(category)
            .name("후라이드치킨")
            .price(16000)
            .description("맛있는 후라이드치킨")
            .pictureUrl("fried-chicken.jpg")
            .build();

        Menu menu2 = Menu.builder()
            .category(category)
            .name("양념치킨")
            .price(17000)
            .description("맛있는 양념치킨")
            .pictureUrl("seasoned-chicken.jpg")
            .build();
        menuRepository.saveAll(List.of(menu1, menu2));

        // when
        Menu findMenu = menuQueryRepository.findOne(menu1.getId()).orElse(null);

        // then
        assertThat(findMenu).extracting("name", "price", "description", "pictureUrl")
            .containsExactly("후라이드치킨", 16000, "맛있는 후라이드치킨", "fried-chicken.jpg");
    }

}