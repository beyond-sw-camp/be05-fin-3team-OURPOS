package com.ourpos.domain.menu;

import static com.ourpos.domain.menu.QCategory.*;
import static com.ourpos.domain.menu.QMenu.*;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CategoryQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<Category> findAllCategory() {
		return queryFactory
			.selectFrom(category)
			.where(isNotDelete())
			.fetch();
	}

	public Optional<Category> findOne(Long categoryId) {
		return Optional.ofNullable(queryFactory
			.selectFrom(category)
			.where(categoryIdEq(categoryId))
			.fetchFirst());
	}

	private static BooleanExpression categoryIdEq(Long categoryId) {
		return category.id.eq(categoryId);
	}

	private static BooleanExpression isNotDelete() {
		return menu.deletedYn.eq(false);
	}

}
