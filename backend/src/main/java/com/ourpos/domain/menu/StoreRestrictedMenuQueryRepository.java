package com.ourpos.domain.menu;

import static com.ourpos.domain.menu.QStoreRestrictedMenu.*;
import static com.ourpos.domain.store.QStore.*;

import java.util.List;


import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Repository
@Slf4j
public class StoreRestrictedMenuQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<StoreRestrictedMenu> findAllWithStoreId(Long storeId) {

        return queryFactory
            .selectFrom(storeRestrictedMenu)
            .where(storeEq(storeId))
            .fetch();
    }

    private static BooleanExpression storeEq(Long storeId) {
        return store != null ? storeRestrictedMenu.store.id.eq(storeId) : null;
    }

}
