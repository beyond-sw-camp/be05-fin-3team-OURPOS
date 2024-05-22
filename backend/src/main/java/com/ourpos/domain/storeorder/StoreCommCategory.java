package com.ourpos.domain.storeorder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreCommCategory {
    INGREDIENT("식자재"), SUPPLIES("비품");

    private final String text;
}
