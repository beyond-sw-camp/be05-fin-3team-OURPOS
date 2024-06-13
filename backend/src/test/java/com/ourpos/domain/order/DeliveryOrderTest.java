package com.ourpos.domain.order;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.orderdetail.OrderOption;
import com.ourpos.domain.orderdetail.OrderOptionGroup;
import com.ourpos.domain.rider.Rider;
import com.ourpos.domain.store.Store;

class DeliveryOrderTest {

    @DisplayName("주문 가격을 계산한다.")
    @Test
    void calculateOrderPrice() {
        // given
        Customer customer = createCustomer();
        Store store = createStore();
        OrderAddress orderAddress = createOrderAddress();

        Menu menu1 = createMenu("햄버거", 5000);
        Menu menu2 = createMenu("콜라", 3000);

        OrderOption orderOption1 = createOrderOption("치즈 추가", 500);
        OrderOption orderOption2 = createOrderOption("피클 추가", 300);
        OrderOption orderOption3 = createOrderOption("감자튀김 추가", 1000);
        OrderOption orderOption4 = createOrderOption("패티 추가", 2000);

        OrderOptionGroup orderOptionGroup1 = createOrderOptionGroup("햄버거 옵션", orderOption1, orderOption2, orderOption4);
        OrderOptionGroup orderOptionGroup2 = createOrderOptionGroup("감자튀김 옵션", orderOption3);

        OrderDetail orderDetail1 = createOrderDetail(menu1, 2, orderOptionGroup1, orderOptionGroup2);
        OrderDetail orderDetail2 = createOrderDetail(menu2, 1, orderOptionGroup1);

        // when
        DeliveryOrder deliveryOrder = createDeliveryOrder(customer, store, orderAddress, orderDetail1, orderDetail2);

        // then
        assertThat(orderDetail1.getPrice()).isEqualTo(
            (menu1.getPrice() + orderOption1.getPrice() + orderOption2.getPrice() + orderOption3.getPrice()
                + orderOption4.getPrice()) * orderDetail1.getQuantity());
        assertThat(orderDetail2.getPrice()).isEqualTo(
            menu2.getPrice() + orderOption1.getPrice() + orderOption2.getPrice() + orderOption4.getPrice());

        assertThat(deliveryOrder.getPrice()).isEqualTo(orderDetail1.getPrice() + orderDetail2.getPrice());
    }

    @DisplayName("배달 주문 상태 변경: 준비 중 -> 조리 중")
    @Test
    void changeDeliveryOrderStatusFromReadyToCooking() {
        // given
        DeliveryOrder deliveryOrder = createDeliveryOrder(createCustomer(), createStore(), createOrderAddress());

        // when
        deliveryOrder.acceptOrder();

        // then
        assertThat(deliveryOrder.getStatus()).isEqualTo(DeliveryStatus.COOKING);
    }

    @DisplayName("배달 주문 상태 변경: 조리 중 -> 배달 중")
    @Test
    void changeDeliveryOrderStatusFromCookingToDelivering() {
        // given
        DeliveryOrder deliveryOrder = createDeliveryOrder(createCustomer(), createStore(), createOrderAddress());
        deliveryOrder.acceptOrder();

        // when
        deliveryOrder.startDelivery();

        // then
        assertThat(deliveryOrder.getStatus()).isEqualTo(DeliveryStatus.DELIVERING);
    }

    @DisplayName("배달 주문 상태 변경: 배달 중 -> 완료")
    @Test
    void changeDeliveryOrderStatusFromDeliveringToCompleted() {
        // given
        DeliveryOrder deliveryOrder = createDeliveryOrder(createCustomer(), createStore(), createOrderAddress());
        deliveryOrder.acceptOrder();
        deliveryOrder.startDelivery();

        // when
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        deliveryOrder.completeOrder(now);

        // then
        assertThat(deliveryOrder.getStatus()).isEqualTo(DeliveryStatus.COMPLETED);
    }

    @DisplayName("배달 주문 상태 변경: 대기 중 -> 취소")
    @Test
    void changeDeliveryOrderStatusFromWaitingToCanceled() {
        // given
        DeliveryOrder deliveryOrder = createDeliveryOrder(createCustomer(), createStore(), createOrderAddress());

        // when
        deliveryOrder.cancelOrder();

        // then
        assertThat(deliveryOrder.getStatus()).isEqualTo(DeliveryStatus.CANCELED);
    }

    @DisplayName("배달 주문 상태 변경: 대기 중이 아닌 주문 취소 시도 에러 발생!")
    @Test
    void changeDeliveryOrderStatusFromCookingToCanceled() {
        // given
        DeliveryOrder deliveryOrder = createDeliveryOrder(createCustomer(), createStore(), createOrderAddress());

        // when
        deliveryOrder.acceptOrder();

        // then
        assertThatThrownBy(deliveryOrder::cancelOrder)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("대기중인 주문만 취소할 수 있습니다.");
    }

    @DisplayName("배달 주문 상태 변경: 배달 중이 아닌 주문 완료 시도 에러 발생!")
    @Test
    void changeDeliveryOrderStatusFromWaitingToCompleted() {
        // given
        DeliveryOrder deliveryOrder = createDeliveryOrder(createCustomer(), createStore(), createOrderAddress());

        // when
        deliveryOrder.acceptOrder();

        // then
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        assertThatThrownBy(() -> {
            deliveryOrder.completeOrder(now);
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("배달중인 주문만 완료할 수 있습니다.");
    }

    @DisplayName("배달 주문 라이더 배정")
    @Test
    void assignRider() {
        // given
        DeliveryOrder deliveryOrder = createDeliveryOrder(createCustomer(), createStore(), createOrderAddress());
        Rider rider = Rider.builder().name("라이더1").phone("010-1234-5678").build();

        // when
        deliveryOrder.assignRider(rider);

        // then
        assertThat(deliveryOrder.getRider()).isEqualTo(rider);
    }

    @DisplayName("배달 주문 라이더 배정: 대기 중 또는 조리 중이 아닌 주문에서 라이더 배정 시도 에러 발생!")
    @Test
    void assignRiderToWaitingDeliveryOrder() {
        // given
        DeliveryOrder deliveryOrder = createDeliveryOrder(createCustomer(), createStore(), createOrderAddress());
        Rider rider = Rider.builder().name("라이더1").phone("010-1234-5678").build();

        // when
        deliveryOrder.acceptOrder();
        deliveryOrder.startDelivery();

        // then
        assertThatThrownBy(() -> deliveryOrder.assignRider(rider))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("대기중 또는 조리중인 주문만 라이더를 배정할 수 있습니다.");
    }

    @DisplayName("배달 주문 배달 예상 시간 설정")
    @Test
    void setEstimatedTime() {
        // given
        DeliveryOrder deliveryOrder = createDeliveryOrder(createCustomer(), createStore(), createOrderAddress());
        deliveryOrder.acceptOrder();
        deliveryOrder.assignRider(Rider.builder().name("라이더1").phone("010-1234-5678").build());

        // when
        LocalTime now = LocalTime.of(12, 0, 0);
        LocalTime estimatedTime = now.plusMinutes(30);
        deliveryOrder.setEstimatedTime(estimatedTime);

        // then
        assertThat(deliveryOrder.getEstimatedTime()).isEqualToIgnoringSeconds(estimatedTime);
    }

    private static DeliveryOrder createDeliveryOrder(Customer customer, Store store, OrderAddress orderAddress,
        OrderDetail... orderDetails) {
        return DeliveryOrder.builder()
            .customer(customer)
            .store(store)
            .riderMessage("문 앞에 놔주세요.")
            .ownerMessage("조심히 와주세요.")
            .tip(1000)
            .disposableYn(true)
            .orderAddress(orderAddress)
            .orderDetails(List.of(orderDetails))
            .build();
    }

    private static OrderOptionGroup createOrderOptionGroup(String name, OrderOption... orderOptions) {
        return OrderOptionGroup.builder()
            .name(name)
            .orderOptions(List.of(orderOptions))
            .build();
    }

    private static OrderOption createOrderOption(String name, int price) {
        return OrderOption.builder()
            .name(name)
            .price(price)
            .build();
    }

    private static OrderDetail createOrderDetail(Menu menu, int quantity, OrderOptionGroup... orderOptionGroups) {
        return OrderDetail.builder()
            .menu(menu)
            .quantity(quantity)
            .orderOptionGroups(List.of(orderOptionGroups))
            .build();
    }

    private static Menu createMenu(String name, int price) {
        return Menu.builder()
            .name(name)
            .price(price)
            .build();
    }

    private static Store createStore() {
        return Store.builder()
            .name("강남점")
            .phone("02-1234-5678")
            .minimumOrderPrice(10000)
            .build();
    }

    private static Customer createCustomer() {
        return Customer.builder()
            .name("홍길동")
            .phone("010-1234-5678")
            .build();
    }

    private static OrderAddress createOrderAddress() {
        return OrderAddress.builder()
            .addressBase("서울시 강남구 역삼동 123-45")
            .addressDetail("우성아파트 101동 202호")
            .build();
    }

}