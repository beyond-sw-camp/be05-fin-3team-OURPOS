package com.ourpos.domain.order;

import static com.ourpos.domain.order.QDeliveryOrder.*;
import static com.ourpos.domain.order.QHallOrder.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
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

    public Optional<HallOrder> findOneHallOrder(Long orderId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer)
            .fetchJoin()
            .join(hallOrder.store)
            .fetchJoin()
            .where(orderEq(orderId))
            .fetchFirst());
    }

    private static BooleanExpression orderEq(Long orderId) {
        return hallOrder.id.eq(orderId);
    }

    public Optional<DeliveryOrder> findOneDeliveryOrder(Long orderId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(deliveryOrder)
            .join(deliveryOrder.customer)
            .join(deliveryOrder.store)
            .where(orderEq(orderId))
            .fetchFirst());
    }
}
