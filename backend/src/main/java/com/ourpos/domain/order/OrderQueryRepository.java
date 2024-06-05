package com.ourpos.domain.order;

import static com.ourpos.domain.order.QDeliveryOrder.*;
import static com.ourpos.domain.order.QHallOrder.*;
import static com.ourpos.domain.order.QOrder.*;
import static com.ourpos.domain.orderdetail.QOrderDetail.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.ourpos.api.order.dto.response.CountMonthlyResponseDto;
import com.ourpos.api.order.dto.response.MealTimeResponseDto;
import com.ourpos.api.order.dto.response.MealTypeResponseDto;
import com.ourpos.api.order.dto.response.MenuPreferResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Repository
@Slf4j
public class OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 상태 홀 주문 확인
    public Page<HallOrder> findHallOrders(Long storeId, String status, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(hallOrder.store.id.eq(storeId));

        if (status != null && !status.isEmpty()) {
            builder.and(hallOrder.status.eq(HallStatus.valueOf(status)));

            if (HallStatus.valueOf(status) == HallStatus.COMPLETED) {
                LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
                builder.and(hallOrder.completedDateTime.after(threeDaysAgo));
            }
        }

        List<HallOrder> content = queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer).fetchJoin()
            .join(hallOrder.store).fetchJoin()
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // count query
        JPAQuery<HallOrder> countQuery = queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer).fetchJoin()
            .join(hallOrder.store).fetchJoin()
            .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    public Optional<HallOrder> findOneHallOrder(Long orderId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer).fetchJoin()
            .join(hallOrder.store).fetchJoin()
            .where(hallOrderEq(orderId))
            .fetchFirst());
    }

    public Optional<DeliveryOrder> findOneDeliveryOrder(Long orderId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(deliveryOrder)
            .join(deliveryOrder.customer).fetchJoin()
            .join(deliveryOrder.store).fetchJoin()
            .join(deliveryOrder.orderAddress).fetchJoin()
            .where(deliveryOrderEq(orderId))
            .fetchFirst());
    }

    // 상태에 따른 배달 목록 확인
    public Page<DeliveryOrder> findDeliveryOrders(Long storeId, String status, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(deliveryOrder.store.id.eq(storeId));

        if (status != null && !status.isEmpty()) {
            builder.and(deliveryStatusEq(status));

            if (DeliveryStatus.valueOf(status) == DeliveryStatus.COMPLETED) {
                LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
                builder.and(deliveryOrder.completedDateTime.after(threeDaysAgo));
            }
        }

        List<DeliveryOrder> content = queryFactory
            .selectFrom(deliveryOrder)
            .join(deliveryOrder.customer).fetchJoin()
            .join(deliveryOrder.store).fetchJoin()
            .join(deliveryOrder.orderAddress).fetchJoin()
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // count query
        JPAQuery<DeliveryOrder> countQuery = queryFactory
            .selectFrom(deliveryOrder)
            .join(deliveryOrder.customer).fetchJoin()
            .join(deliveryOrder.store).fetchJoin()
            .join(deliveryOrder.orderAddress).fetchJoin()
            .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    public Page<HallOrder> findHallOrdersByLoginId(String loginId, Pageable pageable) {
        List<HallOrder> content = queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer).fetchJoin()
            .join(hallOrder.store).fetchJoin()
            .where(hallOrder.customer.loginId.eq(loginId))
            .orderBy(hallOrder.createdDateTime.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // count query
        JPAQuery<HallOrder> countQuery = queryFactory
            .selectFrom(hallOrder)
            .join(hallOrder.customer).fetchJoin()
            .join(hallOrder.store).fetchJoin()
            .where(hallOrder.customer.loginId.eq(loginId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    public Page<DeliveryOrder> findDeliveryOrdersByLoginId(String loginId, String status, Pageable pageable) {
        List<DeliveryOrder> content = queryFactory
            .selectFrom(deliveryOrder)
            .join(deliveryOrder.customer).fetchJoin()
            .join(deliveryOrder.store).fetchJoin()
            .join(deliveryOrder.orderAddress).fetchJoin()
            .where(deliveryLoginIdEq(loginId), deliveryStatusEq(status))
            .orderBy(deliveryOrder.createdDateTime.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // count query
        JPAQuery<DeliveryOrder> countQuery = queryFactory
            .selectFrom(deliveryOrder)
            .join(deliveryOrder.customer).fetchJoin()
            .join(deliveryOrder.store).fetchJoin()
            .join(deliveryOrder.orderAddress).fetchJoin()
            .where(deliveryLoginIdEq(loginId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private static BooleanExpression deliveryStatusEq(String status) {
        return status != null ? deliveryOrder.status.eq(DeliveryStatus.valueOf(status)) : null;
    }

    private static BooleanExpression deliveryLoginIdEq(String loginId) {
        return deliveryOrder.customer.loginId.eq(loginId);
    }

    private static BooleanExpression hallOrderEq(Long orderId) {
        return hallOrder.id.eq(orderId);
    }

    private static BooleanExpression deliveryOrderEq(Long orderId) {
        return deliveryOrder.id.eq(orderId);
    }

    // 월별 매출량
    public List<CountMonthlyResponseDto> countMonthly(Long storeId) {

        BooleanBuilder builder = new BooleanBuilder();

        // builder에 조건 담기
        if (storeId != null) {
            builder.and(order.store.id.eq(storeId));
        }
        builder.and(
            (deliveryOrder.status.ne(DeliveryStatus.WAITING)
                .and(deliveryOrder.status.ne(DeliveryStatus.CANCELED)))
                .or(
                    hallOrder.status.ne(HallStatus.WAITING)
                        .and(hallOrder.status.ne(HallStatus.CANCELED))
                )
        );

        return queryFactory
            .select(Projections.constructor(CountMonthlyResponseDto.class,
                order.store.id, order.store.name,
                order.createdDateTime.year().as("year"),
                order.createdDateTime.month().as("month"),
                order.price.sum().as("total")))
            .from(order)
            .groupBy(order.createdDateTime.year(), order.createdDateTime.month(), order.store.id, order.store.name)
            .join(order.store)
            .leftJoin(deliveryOrder)
            .on(order.id.eq(deliveryOrder.id))
            .leftJoin(hallOrder)
            .on(order.id.eq(hallOrder.id))
            .where(builder)
            .fetch();
    }

    // 식사 유형에 따른 매출액 비중
    public List<MealTypeResponseDto> mealType(Long storeId) {

        Integer hallTotal = queryFactory
            .select(hallOrder.price.sum())
            .from(hallOrder)
            .join(hallOrder.store)
            .groupBy(hallOrder.store.id)
            .where(hallOrder.store.id.eq(storeId), hallOrder.status.ne(HallStatus.WAITING))
            .fetchOne();

        Integer deliveryTotal = queryFactory
            .select(deliveryOrder.price.sum())
            .from(deliveryOrder)
            .join(deliveryOrder.store)
            .groupBy(deliveryOrder.store.id)
            .where(deliveryOrder.store.id.eq(storeId), deliveryOrder.status.ne(DeliveryStatus.WAITING))
            .fetchOne();

        MealTypeResponseDto hallDto = new MealTypeResponseDto("hall", hallTotal);
        MealTypeResponseDto deliveryDto = new MealTypeResponseDto("delivery", deliveryTotal);

        List<MealTypeResponseDto> dtos = new ArrayList<>();
        dtos.add(hallDto);
        dtos.add(deliveryDto);

        return dtos;
    }

    // 시간대별 매출 발생 추이
    public List<MealTimeResponseDto> mealTime(Long storeId) {

        BooleanBuilder builder = new BooleanBuilder();

        // builder에 조건 담기
        if (storeId != null) {
            builder.and(order.store.id.eq(storeId));
        }

        builder.and(
            (deliveryOrder.status.ne(DeliveryStatus.WAITING)
                .and(deliveryOrder.status.ne(DeliveryStatus.CANCELED)))
                .or(
                    hallOrder.status.ne(HallStatus.WAITING)
                        .and(hallOrder.status.ne(HallStatus.CANCELED))
                )
        );

        return queryFactory
            .select(Projections.constructor(MealTimeResponseDto.class,
                order.store.id, order.store.name,
                order.createdDateTime.hour().as("hour"),
                order.price.sum().as("total")))
            .from(order)
            .join(order.store)
            .leftJoin(deliveryOrder).on(order.id.eq(deliveryOrder.id))
            .leftJoin(hallOrder).on(order.id.eq(hallOrder.id))
            .where(builder)
            .groupBy(order.createdDateTime.hour(), order.store.id, order.store.name)
            .fetch();
    }

    // 메뉴별 주문 비중 ->
    public List<MenuPreferResponseDto> menuPrefer(Long storeId) {

        BooleanBuilder builder = new BooleanBuilder();

        // builder에 조건 담기
        if (storeId != null) {
            builder.and(order.store.id.eq(storeId));
        }

        return queryFactory
            .select(Projections.constructor(MenuPreferResponseDto.class,
                orderDetail.menu.id,
                orderDetail.menu.name,
                orderDetail.menu.category.name,
                orderDetail.order.countDistinct().as("quantity")))
            .from(orderDetail)
            .groupBy(orderDetail.menu.id, orderDetail.menu.name)
            .join(orderDetail.order)
            .join(orderDetail.menu)
            .join(orderDetail.menu.category)
            .where(builder)
            .fetch();

    }

    // 배달 주소 검색
    public List<String> deliveryFrequency(Long storeId) {
        return queryFactory
            .select(deliveryOrder.orderAddress.addressBase)
            .from(deliveryOrder)
            .join(deliveryOrder.orderAddress)
            .join(deliveryOrder.store)
            .where(deliveryOrder.store.id.eq(storeId))
            .fetch();
    }

    public List<MealTypeResponseDto> mealTypeAll() {

        Integer hallTotal = queryFactory
            .select(hallOrder.price.sum())
            .from(hallOrder)
            .where(hallOrder.status.ne(HallStatus.WAITING)
                .and(hallOrder.status.ne(HallStatus.CANCELED)))
            .fetchOne();

        Integer deliveryTotal = queryFactory
            .select(deliveryOrder.price.sum())
            .from(deliveryOrder)
            .where(
                deliveryOrder.status.ne(DeliveryStatus.WAITING)
                    .and(deliveryOrder.status.ne(DeliveryStatus.CANCELED)))
            .fetchOne();

        MealTypeResponseDto hallDto = new MealTypeResponseDto("hall", hallTotal);
        MealTypeResponseDto deliveryDto = new MealTypeResponseDto("delivery", deliveryTotal);

        List<MealTypeResponseDto> list = new ArrayList<>();
        list.add(hallDto);
        list.add(deliveryDto);
        return list;

    }

}
