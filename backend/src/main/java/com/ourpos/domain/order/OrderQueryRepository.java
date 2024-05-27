package com.ourpos.domain.order;

import static com.ourpos.domain.order.QDeliveryOrder.*;
import static com.ourpos.domain.order.QHallOrder.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
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
            .join(hallOrder.customer)
            .fetchJoin()
            .join(hallOrder.store)
            .fetchJoin()
            .where(hallOrder.store.id.eq(storeId), hallOrder.status.eq(HallStatus.valueOf(status)))
            .where(hallOrder.store.id.eq(storeId), hallOrder.status.eq(HallStatus.valueOf(status)))

            .offset(offset)
            .limit(limit)
            .fetch();
    }

    // 상태 홀 주문 확인
    public List<HallOrder> findHallOrder(Long storeId, String status) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(hallOrder.store.id.eq(storeId));

        if (status != null && !status.isEmpty()) {
            builder.and(hallOrder.status.eq(HallStatus.valueOf(status)));

            if (HallStatus.valueOf(status) == HallStatus.COMPLETED) {
                LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
                builder.and(hallOrder.completedDateTime.after(threeDaysAgo));
            }
        }

        return queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer).fetchJoin()
            .join(hallOrder.store).fetchJoin()
            .where(builder)
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

    // 상태에 따른 배달 목록 확인
    public List<DeliveryOrder> findDeliveryOrder(Long storeId, String status) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(deliveryOrder.store.id.eq(storeId));

        if (status != null && !status.isEmpty()) {
            builder.and(deliveryOrder.status.eq(DeliveryStatus.valueOf(status)));

            if (DeliveryStatus.valueOf(status) == DeliveryStatus.COMPLETED) {
                LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
                builder.and(deliveryOrder.completedDateTime.after(threeDaysAgo));
            }
        }

        return queryFactory
            .selectFrom(deliveryOrder)
            .join(deliveryOrder.customer).fetchJoin()
            .join(deliveryOrder.store).fetchJoin()
            .join(deliveryOrder.orderAddress).fetchJoin()
            .where(builder)
            .fetch();
    }

    public List<HallOrder> findOneHallOrderByLoginId(String loginId) {
        return queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer).fetchJoin()
            .join(hallOrder.store).fetchJoin()
            .where(hallOrder.customer.loginId.eq(loginId))
            .fetch();
    }

    public List<DeliveryOrder> findOneDeliveryOrderByLoginId(String loginId) {
        return queryFactory
            .selectFrom(deliveryOrder)
            .join(deliveryOrder.customer).fetchJoin()
            .join(deliveryOrder.store).fetchJoin()
            .join(deliveryOrder.orderAddress).fetchJoin()
            .where(deliveryOrder.customer.loginId.eq(loginId))
            .fetch();
    }

    private static BooleanExpression deliveryOrderEq(Long orderId) {
        return deliveryOrder.id.eq(orderId);
    }
}
