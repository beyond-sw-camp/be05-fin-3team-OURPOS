package com.ourpos.domain.order;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.orderdetail.OrderOption;
import com.ourpos.domain.orderdetail.OrderOptionGroup;
import com.ourpos.domain.store.Store;

class OrderTest {

    @DisplayName("메뉴와 메뉴 옵션을 포함한 주문 가격 계산")
    @Test
    void calculateOrder() {
        // given
        OrderOption orderOption1 = OrderOption.builder()
            .name("피클")
            .price(500)
            .build();

        OrderOption orderOption2 = OrderOption.builder()
            .name("치즈")
            .price(1000)
            .build();

        OrderOption orderOption3 = OrderOption.builder()
            .name("양념")
            .price(700)
            .build();

        OrderOptionGroup orderOptionGroup1 = OrderOptionGroup.builder()
            .name("토핑 선택")
            .orderOption(orderOption1)
            .orderOption(orderOption2)
            .build();

        OrderOptionGroup orderOptionGroup2 = OrderOptionGroup.builder()
            .name("소스 선택")
            .orderOption(orderOption3)
            .build();

        Store store = Store.builder()
            .name("강남점")
            .build();

        Menu menu = Menu.builder()
            .name("햄버거")
            .price(5000)
            .build();

        OrderDetail orderDetail = OrderDetail.builder()
            .menu(menu)
            .quantity(2)
            .orderOptionGroup(orderOptionGroup1)
            .orderOptionGroup(orderOptionGroup2)
            .build();

        Order order = HallOrder.builder()
            .store(store)
            .orderTakeoutYn(false)
            .orderDetail(orderDetail)
            .build();

        // when then
        assertThat(order.getPrice()).isEqualTo(5000 * 2 + 500 + 1000 + 700);
    }

}