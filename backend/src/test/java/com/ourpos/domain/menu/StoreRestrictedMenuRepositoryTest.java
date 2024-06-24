package com.ourpos.domain.menu;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class StoreRestrictedMenuRepositoryTest {

    @Autowired
    private StoreRestrictedMenuRepository storeRestrictedMenuRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("가게 아이디로 가게에 등록된 메뉴를 조회할 수 있다.")
    @Test
    void findByStoreId() {
        // given
        Store store = Store.builder()
            .name("치킨집")
            .build();

        storeRepository.save(store);

        Menu menu1 = Menu.builder()
            .name("후라이드치킨")
            .price(16000)
            .description("맛있는 후라이드치킨")
            .pictureUrl("fried-chicken.jpg")
            .build();

        Menu menu2 = Menu.builder()
            .name("양념치킨")
            .price(17000)
            .description("맛있는 양념치킨")
            .pictureUrl("seasoned-chicken.jpg")
            .build();
        menuRepository.saveAll(List.of(menu1, menu2));

        StoreRestrictedMenu storeRestrictedMenu1 = StoreRestrictedMenu.builder()
            .store(store)
            .menu(menu1)
            .build();

        StoreRestrictedMenu storeRestrictedMenu2 = StoreRestrictedMenu.builder()
            .store(store)
            .menu(menu2)
            .build();

        storeRestrictedMenuRepository.saveAll(List.of(storeRestrictedMenu1, storeRestrictedMenu2));

        // when
        List<StoreRestrictedMenu> findRestrictedMenu = storeRestrictedMenuRepository.findByStoreId(store.getId());

        // then
        assertThat(findRestrictedMenu).extracting("menu.name", "menu.price", "menu.description", "menu.pictureUrl")
            .containsExactlyInAnyOrder(
                tuple("후라이드치킨", 16000, "맛있는 후라이드치킨", "fried-chicken.jpg"),
                tuple("양념치킨", 17000, "맛있는 양념치킨", "seasoned-chicken.jpg")
            );
    }

}