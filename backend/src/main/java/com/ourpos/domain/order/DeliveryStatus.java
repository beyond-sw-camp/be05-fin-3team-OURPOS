package com.ourpos.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    WAITING("대기 중"), COOKING("조리 중"), DELIVERING("배달 중"), COMPLETED("배달 완료"), CANCELED("주문 취소");

    private final String text;
}
