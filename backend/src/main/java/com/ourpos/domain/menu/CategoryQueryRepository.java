package com.ourpos.domain.menu;

import static com.ourpos.domain.menu.QCategory.*;

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

	public List<Category> findAllCategories() {
		return queryFactory
			.selectFrom(category)
			.where(category.deletedYn.eq(false))
			.fetch();
	}

	public Optional<Category> findOne(Long categoryId) {
		return Optional.ofNullable(queryFactory
			.selectFrom(category)
			.where(category.id.eq(categoryId))
			.distinct()
			.fetchFirst());
	}

	// public Optional<Category> findOne(Long categoryId) {
	// 	return Optional.ofNullable(queryFactory
	// 		.selectFrom(category)
	// 		.leftJoin(category.menuOptionGroups, menuOptionGroup).fetchJoin()
	// 		.leftJoin(menuOptionGroup.menuOptions, menuOption).fetchJoin()
	// 		.where(category.id.eq(categoryId)
	// 			.and(menuOptionGroup.deletedYn.eq(false))
	// 			.and(menuOption.deletedYn.eq(false)))
	// 		.distinct()
	// 		.fetchFirst());
	// }

	private static BooleanExpression categoryIdEq(Long categoryId) {
		return category.id.eq(categoryId);
	}

}
