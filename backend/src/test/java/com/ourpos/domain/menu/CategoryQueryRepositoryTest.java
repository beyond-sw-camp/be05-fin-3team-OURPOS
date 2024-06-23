package com.ourpos.domain.menu;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.category.service.CategoryService;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class CategoryQueryRepositoryTest {

    @Autowired
    private CategoryQueryRepository categoryQueryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @DisplayName("삭제 처리가 되지 않은 모든 카테고리를 조회할 수 있다.")
    @Test
    void findAllCategories() {
        // given
        Category category1 = Category.builder()
            .name("치킨")
            .build();

        Category category2 = Category.builder()
            .name("피자")
            .build();

        Category category3 = Category.builder()
            .name("사이드")
            .build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        categoryService.deleteCategory(category3.getId());

        // when
        List<Category> allCategories = categoryQueryRepository.findAllCategories();

        // then
        assertThat(allCategories).hasSize(2)
            .extracting("name")
            .containsExactly("치킨", "피자");
    }

    @DisplayName("삭제 처리가 되지 않은 카테고리 하나를 조회할 수 있다.")
    @Test
    void findOne() {
        // given
        Category category1 = Category.builder()
            .name("치킨")
            .build();

        Category category2 = Category.builder()
            .name("피자")
            .build();

        Category category3 = Category.builder()
            .name("사이드")
            .build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        categoryService.deleteCategory(category3.getId());

        // when
        Category one = categoryQueryRepository.findOne(category2.getId()).get();

        // then
        assertThat(one.getName()).isEqualTo("피자");
    }

}