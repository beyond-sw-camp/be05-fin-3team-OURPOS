package com.ourpos.domain.storeorder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreCommStatus {
    WAITING("대기 중"), COOKING("조리 중"), COMPLETED("조리 완료"), CANCELED("주문 취소");

    private final String text;
}
