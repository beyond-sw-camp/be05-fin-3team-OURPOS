package com.ourpos.domain.store;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ourpos.domain.storeorder.StoreComm;

class StoreStockTest {

    @DisplayName("가게 재고가 충분한 경우, 수량을 감소시킨다.")
    @Test
    void decreaseQuantity() {
        // given
        Store store = Store.builder()
            .name("store")
            .build();

        StoreComm storeComm = StoreComm.builder()
            .name("storeComm")
            .build();

        StoreStock storeStock = StoreStock.builder()
            .store(store)
            .storeComm(storeComm)
            .quantity(10)
            .build();
        // when
        storeStock.decreaseQuantity(5);

        // then
        assertThat(storeStock.getQuantity()).isEqualTo(5);
    }

    @DisplayName("가게 재고가 부족한 경우, 예외를 발생시킨다.")
    @Test
    void decreaseQuantity_NotEnoughStock() {
        // given
        Store store = Store.builder()
            .name("store")
            .build();

        StoreComm storeComm = StoreComm.builder()
            .name("storeComm")
            .build();

        StoreStock storeStock = StoreStock.builder()
            .store(store)
            .storeComm(storeComm)
            .quantity(10)
            .build();
        // when
        // then
        assertThatThrownBy(() -> storeStock.decreaseQuantity(15))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("재고가 부족합니다.");
    }

}