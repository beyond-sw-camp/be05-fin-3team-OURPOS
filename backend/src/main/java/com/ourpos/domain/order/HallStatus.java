package com.ourpos.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HallStatus {
    WAITING("대기"), COMPLETED("완료"), CANCELED("취소");

    private final String text;
}
