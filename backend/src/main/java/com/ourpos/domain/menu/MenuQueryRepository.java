package com.ourpos.domain.menu;

import static com.ourpos.domain.menu.QMenu.*;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Repository
@Slf4j
public class MenuQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<Menu> findAllWithCategory(String category) {

		return queryFactory
			.selectFrom(menu)
			.join(menu.category).fetchJoin()
			.where(categoryEq(category), isNotDelete(), menu.deletedYn.eq(false))
			.fetch();
	}

	public Optional<Menu> findOne(Long menuId) {
		return Optional.ofNullable(queryFactory
			.selectFrom(menu)
			.where(menuIdEq(menuId), menu.deletedYn.eq(false))
			.fetchFirst());
	}

	private static BooleanExpression categoryEq(String category) {
		return category != null ? menu.category.name.eq(category) : null;
	}

	private static BooleanExpression isNotDelete() {
		return menu.deletedYn.eq(false);
	}

	private static BooleanExpression menuIdEq(Long menuId) {
		return menu.id.eq(menuId);
	}
}
