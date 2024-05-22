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

    public List<HallOrder> findHallOrderByStoreId(Long storeId, String status, int offset, int limit) {
        return queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer).fetchJoin()
            .join(hallOrder.store).fetchJoin()
            .where(hallOrder.store.id.eq(storeId))
            .where(hallOrder.status.eq(HallStatus.valueOf(status)))
            .offset(offset)
            .limit(limit)
            .fetch();
    }

    // 대기 상태인 주문 확인
    public List<HallOrder> findWaitingAll(Long storeId) {
        return queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer).fetchJoin()  // Fetch join 사용
            .join(hallOrder.store).fetchJoin()     // Fetch join 사용
            .where(hallOrder.status.eq(HallStatus.valueOf("WAITING")) // 조건 추가
                .and(hallOrder.store.id.eq(storeId)))
            .fetch();
    }

    // 조리 상태인 주문 확인
    public List<HallOrder> findCookingAll(Long storeId) {
        return queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer).fetchJoin()  // Fetch join 사용
            .join(hallOrder.store).fetchJoin()     // Fetch join 사용
            .where(hallOrder.status.eq(HallStatus.valueOf("COOKING")) // 조건 추가
                .and(hallOrder.store.id.eq(storeId)))
            .fetch();
    }

    // 완료 상태인 주문 확인
    public List<HallOrder> findCompleteAll(Long storeId) {
        return queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer).fetchJoin()  // Fetch join 사용
            .join(hallOrder.store).fetchJoin()     // Fetch join 사용
            .where(hallOrder.status.eq(HallStatus.valueOf("COMPLETED")) // 조건 추가
                .and(hallOrder.store.id.eq(storeId)))
            .fetch();
    }

    public Optional<HallOrder> findOneHallOrder(Long orderId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer)
            .fetchJoin()
            .join(hallOrder.store)
            .fetchJoin()
            .where(hallOrderEq(orderId))
            .fetchFirst());
    }

    private static BooleanExpression hallOrderEq(Long orderId) {
        return hallOrder.id.eq(orderId);
    }

    public Optional<DeliveryOrder> findOneDeliveryOrder(Long orderId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(deliveryOrder)
            .join(deliveryOrder.customer)
            .fetchJoin()
            .join(deliveryOrder.store)
            .fetchJoin()
            .join(deliveryOrder.orderAddress)
            .fetchJoin()
            .where(deliveryOrderEq(orderId))
            .fetchFirst());
    }

    // 대기 상태인 배달 목록 확인
    public List<DeliveryOrder> findAllDeliveryWaiting(Long storeId) {
        return queryFactory
            .selectFrom(deliveryOrder)
            .join(deliveryOrder.customer)
            .fetchJoin()
            .join(deliveryOrder.store)
            .fetchJoin()
            .join(deliveryOrder.orderAddress)
            .fetchJoin()
            .where(deliveryOrder.status.eq(DeliveryStatus.valueOf("WAITING"))
                .and(deliveryOrder.store.id.eq(storeId)))
            .fetch();
    }

    private static BooleanExpression deliveryOrderEq(Long orderId) {
        return deliveryOrder.id.eq(orderId);
    }
}
