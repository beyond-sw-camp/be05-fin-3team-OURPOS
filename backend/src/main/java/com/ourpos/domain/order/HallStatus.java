package com.ourpos.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HallStatus {
    WAITING("대기 중"), COOKING("조리 중"), COMPLETED("주문 완료"), CANCELED("주문 취소");

    private final String text;
}
