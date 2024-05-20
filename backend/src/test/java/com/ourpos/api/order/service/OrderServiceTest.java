package com.ourpos.api.order.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.order.request.OrderDetailRequestDto;
import com.ourpos.api.order.request.OrderOptionGroupRequestDto;
import com.ourpos.api.order.request.OrderOptionRequestDto;
import com.ourpos.api.order.request.OrderRequestDto;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerRepository;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuRepository;
import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.order.OrderQueryRepository;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderQueryRepository orderQueryRepository;

    @Rollback(false)
    @DisplayName("홀 주문 생성")
    @Test
    void createHallOrder() {
        // given
        Customer customer = Customer.builder()
            .name("홍길동")
            .build();
        customerRepository.save(customer);

        Store store = Store.builder()
            .name("매장1")
            .build();
        storeRepository.save(store);

        Menu menu1 = Menu.builder()
            .name("햄버거")
            .price(5000)
            .build();
        menuRepository.save(menu1);

        Menu menu2 = Menu.builder()
            .name("감자튀김")
            .price(3000)
            .build();
        menuRepository.save(menu2);

        OrderOptionRequestDto orderOptionRequestDto = new OrderOptionRequestDto();
        orderOptionRequestDto.setOptionName("패티 추가");
        orderOptionRequestDto.setPrice(1000);

        OrderOptionGroupRequestDto orderOptionGroupRequestDto = new OrderOptionGroupRequestDto();
        orderOptionGroupRequestDto.setOptionGroupName("사이드 선택");
        orderOptionGroupRequestDto.setOrderOptions(List.of(orderOptionRequestDto));

        OrderDetailRequestDto orderDetailRequestDto1 = new OrderDetailRequestDto();
        orderDetailRequestDto1.setMenuId(menu1.getId());
        orderDetailRequestDto1.setQuantity(1);
        orderDetailRequestDto1.setOrderOptionGroups(List.of(orderOptionGroupRequestDto));

        OrderDetailRequestDto orderDetailRequestDto2 = new OrderDetailRequestDto();
        orderDetailRequestDto2.setMenuId(menu2.getId());
        orderDetailRequestDto2.setQuantity(2);
        orderDetailRequestDto2.setOrderOptionGroups(List.of(orderOptionGroupRequestDto));

        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setCustomerId(customer.getId());
        orderRequestDto.setStoreId(store.getId());
        orderRequestDto.setOrderTakeoutYn(false);
        orderRequestDto.setOrderDetails(List.of(orderDetailRequestDto1, orderDetailRequestDto2));

        // when
        orderService.createHallOrder(orderRequestDto);

        // then
        List<HallOrder> orders = orderQueryRepository.findAll();
        for (HallOrder order : orders) {
            Integer price = order.getOrderDetails()
                .get(0)
                .getOrderOptionGroups()
                .get(0)
                .getOrderOptions()
                .get(0)
                .getPrice();
            System.out.println("price = " + price);
        }
        assertThat(orders).hasSize(1);
    }

}