package com.ourpos.domain.storeorder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StoreOrderStatus {
    WAITING("대기 중"), ACCEPTED("접수 완료"), DELIVERING("배송 중"), COMPLETED("배송 완료"), CANCELED("주문 취소");

    private final String text;
}
