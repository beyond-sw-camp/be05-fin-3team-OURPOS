package com.ourpos.domain.menu;

import static com.ourpos.domain.menu.QMenu.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MenuQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<List<Menu>> findAllWithCategory(Long categoryId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(menu)
            .join(menu.category)
            .where(categoryEq(categoryId))
            .where(isNotDelete())
            .fetch());
    }

    public Optional<Menu> findOne(Long menuId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(menu)
            .where(menuIdEq(menuId))
            .fetchFirst());
    }

    private static BooleanExpression isNotDelete() {
        return menu.deletedYn.eq(false);
    }

    private static BooleanExpression menuIdEq(Long menuId) {
        return menu.id.eq(menuId);
    }

    private static BooleanExpression categoryEq(Long categoryId) {
        return menu.category.id.eq(categoryId);
    }
}
