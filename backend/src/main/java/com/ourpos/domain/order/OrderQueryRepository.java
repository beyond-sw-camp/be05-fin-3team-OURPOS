package com.ourpos.domain.order;

import static com.ourpos.domain.order.QHallOrder.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<HallOrder> findAll() {
        return queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer)
            .join(hallOrder.store)
            .fetch();
    }
}
