package com.ourpos.domain.order;

import static com.ourpos.domain.manager.QManager.*;
import static com.ourpos.domain.menu.QCategory.*;
import static com.ourpos.domain.menu.QMenu.*;
import static com.ourpos.domain.order.QDeliveryOrder.*;
import static com.ourpos.domain.order.QHallOrder.*;
import static com.ourpos.domain.order.QOrder.*;
import static com.ourpos.domain.orderdetail.QOrderDetail.*;
import static com.ourpos.domain.store.QStore.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class AdminOrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 상태 홀 주문 확인
    public Page<HallOrder> findHallOrders(String adminLoginId, String status, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(hallOrderStoreLoginIdEq(adminLoginId));

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
            .orderBy(hallOrder.createdDateTime.desc())
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

    // 상태에 따른 배달 목록 확인
    public Page<DeliveryOrder> findDeliveryOrders(String adminLoginId, String status, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(deliveryOrderStoreLoginIdEq(adminLoginId));

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
            .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private static BooleanExpression deliveryStatusEq(String status) {
        return status != null ? deliveryOrder.status.eq(DeliveryStatus.valueOf(status)) : null;
    }

    // 월별 매출량
    public List<CountMonthlyResponseDto> countMonthly(String adminLoginId) {

        BooleanBuilder builder = new BooleanBuilder();

        // builder에 조건 담기
        if (adminLoginId != null) {
            builder.and(orderStoreLoginIdEq(adminLoginId));
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
    public List<MealTypeResponseDto> mealType(String adminLoginId) {

        Integer hallTotal = queryFactory
            .select(hallOrder.price.sum())
            .from(hallOrder)
            .join(hallOrder.store)
            .groupBy(hallOrder.store.id)
            .where(hallOrderStoreLoginIdEq(adminLoginId), hallOrder.status.ne(HallStatus.WAITING))
            .fetchOne();

        Integer deliveryTotal = queryFactory
            .select(deliveryOrder.price.sum())
            .from(deliveryOrder)
            .join(deliveryOrder.store)
            .groupBy(deliveryOrder.store.id)
            .where(deliveryOrderStoreLoginIdEq(adminLoginId), deliveryOrder.status.ne(DeliveryStatus.WAITING))
            .fetchOne();

        MealTypeResponseDto hallDto = new MealTypeResponseDto("hall", hallTotal);
        MealTypeResponseDto deliveryDto = new MealTypeResponseDto("delivery", deliveryTotal);

        List<MealTypeResponseDto> dtos = new ArrayList<>();
        dtos.add(hallDto);
        dtos.add(deliveryDto);

        return dtos;
    }

    private static BooleanExpression deliveryOrderStoreLoginIdEq(String adminLoginId) {
        return deliveryOrder.store.manager.loginId.eq(adminLoginId);
    }

    private static BooleanExpression hallOrderStoreLoginIdEq(String adminLoginId) {
        return hallOrder.store.manager.loginId.eq(adminLoginId);
    }

    // 시간대별 매출 발생 추이
    public List<MealTimeResponseDto> mealTime(String adminLoginId) {

        BooleanBuilder builder = new BooleanBuilder();

        // builder에 조건 담기
        if (adminLoginId != null) {
            builder.and(orderStoreLoginIdEq(adminLoginId));
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
    public List<MenuPreferResponseDto> menuPrefer(String adminLoginId) {

        BooleanBuilder builder = new BooleanBuilder();

        // builder에 조건 담기
        if (adminLoginId != null) {
            builder.and(orderStoreLoginIdEq(adminLoginId));
        }

        return queryFactory
            .select(Projections.constructor(MenuPreferResponseDto.class,
                orderDetail.menu.id,
                orderDetail.menu.name,
                orderDetail.menu.category.name,
                orderDetail.order.countDistinct().as("quantity")))
            .from(orderDetail)
            .join(orderDetail.order, order)
            .join(order.store, store)
            .join(store.manager, manager)
            .join(orderDetail.menu, menu)
            .join(menu.category, category)
            .where(builder)
            .groupBy(orderDetail.menu.id, orderDetail.menu.name, orderDetail.menu.category.name)
            .fetch();

    }

    private static BooleanExpression orderStoreLoginIdEq(String adminLoginId) {
        return order.store.manager.loginId.eq(adminLoginId);
    }

    // 배달 주소 검색
    public List<String> deliveryFrequency(String adminLoginId) {
        return queryFactory
            .select(deliveryOrder.orderAddress.addressBase)
            .from(deliveryOrder)
            .join(deliveryOrder.orderAddress)
            .join(deliveryOrder.store)
            .where(deliveryOrderStoreLoginIdEq(adminLoginId))
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
