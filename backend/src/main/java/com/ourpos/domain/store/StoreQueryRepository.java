package com.ourpos.domain.store;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class StoreQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Store> findStoresOrderBy(Double latitude, Double longitude) {
        // return queryFactory
        //     .selectFrom(store)
        //     .orderBy(store.location.distance(latitude, longitude).asc())
        //     .fetchOne();
        return null;
    }
}
