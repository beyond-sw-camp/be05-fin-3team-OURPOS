package com.ourpos.domain.order;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HallOrderTest {

    @DisplayName("주문 상태 변경: 대기 중 -> 조리 중")
    @Test
    void changeHallOrderStatusFromWaitingToCooking() {
        // given
        HallOrder hallOrder = createHallOrder();

        // when
        hallOrder.acceptOrder();

        // then
        assertThat(hallOrder.getStatus()).isEqualTo(HallStatus.COOKING);
    }

    @DisplayName("주문 상태 변경: 조리 중 -> 주문 완료")
    @Test
    void changeHallOrderStatusFromCookingToCompleted() {
        // given
        HallOrder hallOrder = createHallOrder();
        hallOrder.acceptOrder();

        // when
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        hallOrder.completeOrder(now);

        // then
        assertThat(hallOrder.getStatus()).isEqualTo(HallStatus.COMPLETED);
    }

    @DisplayName("주문 상태 변경: 대기 중 -> 취소")
    @Test
    void changeHallOrderStatusFromWaitingToCanceled() {
        // given
        HallOrder hallOrder = createHallOrder();

        // when
        hallOrder.cancelOrder();

        // then
        assertThat(hallOrder.getStatus()).isEqualTo(HallStatus.CANCELED);
    }

    @DisplayName("대기 중이 아닌 주문 취소 시도 에러 발생!")
    @Test
    void changeHallOrderStatusFromCookingToCanceled() {
        // given
        HallOrder hallOrder = createHallOrder();

        // when
        hallOrder.acceptOrder();

        // then
        assertThatThrownBy(hallOrder::cancelOrder)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("대기중인 주문만 취소할 수 있습니다.");
    }

    @DisplayName("대기 중이 아닌 주문 접수 시도 에러 발생!")
    @Test
    void changeHallOrderStatusFromCookingToAccepted() {
        // given
        HallOrder hallOrder = createHallOrder();

        // when
        hallOrder.acceptOrder();

        // then
        assertThatThrownBy(hallOrder::acceptOrder)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("대기중인 주문만 접수할 수 있습니다.");
    }

    @DisplayName("조리 중이 아닌 주문 완료 시도 에러 발생!")
    @Test
    void changeHallOrderStatusFromWaitingToCompleted() {
        // given
        HallOrder hallOrder = createHallOrder();

        // when

        // then
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        assertThatThrownBy(() -> {
            hallOrder.completeOrder(now);
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중인 주문만 완료할 수 있습니다.");
    }

    private HallOrder createHallOrder() {
        return HallOrder.builder()
            .build();
    }

}