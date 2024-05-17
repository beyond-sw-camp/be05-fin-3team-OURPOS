package com.ourpos.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    WAITING("배달 대기"), DELIVERING("배달 중"), DELIVERED("배달 완료"), CANCELED("배달 취소");

    private final String text;
}
