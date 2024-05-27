package com.ourpos.api.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.order.dto.request.HallOrderRequestDto;
import com.ourpos.api.order.dto.request.OrderDetailRequestDto;
import com.ourpos.api.order.dto.request.OrderOptionGroupRequestDto;
import com.ourpos.api.order.dto.request.OrderOptionRequestDto;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerRepository;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuRepository;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;

@Transactional
@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderQueryService orderQueryService;

    private static HallOrderRequestDto createHallOrder(Customer customer, Store store,
        OrderDetailRequestDto orderDetailRequestDto1, OrderDetailRequestDto orderDetailRequestDto2) {
        HallOrderRequestDto hallOrderRequestDto = new HallOrderRequestDto();
        hallOrderRequestDto.setCustomerId(customer.getId());
        hallOrderRequestDto.setStoreId(store.getId());
        hallOrderRequestDto.setOrderTakeoutYn(false);
        hallOrderRequestDto.setOrderDetails(List.of(orderDetailRequestDto1, orderDetailRequestDto2));
        return hallOrderRequestDto;
    }

    private static OrderOptionGroupRequestDto createOrderOptionGroup(
        OrderOptionRequestDto orderOptionRequestDto) {
        OrderOptionGroupRequestDto orderOptionGroupRequestDto = new OrderOptionGroupRequestDto();
        orderOptionGroupRequestDto.setOptionGroupName("사이드 선택");
        orderOptionGroupRequestDto.setOrderOptions(List.of(orderOptionRequestDto));
        return orderOptionGroupRequestDto;
    }

    private static OrderOptionRequestDto createOrderOption() {
        OrderOptionRequestDto orderOptionRequestDto = new OrderOptionRequestDto();
        orderOptionRequestDto.setOptionName("패티 추가");
        orderOptionRequestDto.setPrice(1000);
        return orderOptionRequestDto;
    }

    private Menu createMenu(String name, int price) {
        Menu menu = Menu.builder()
            .name(name)
            .price(price)
            .build();
        menuRepository.save(menu);
        return menu;
    }

    private Store createStore() {
        Store store = Store.builder()
            .name("강남점")
            .build();
        storeRepository.save(store);
        return store;
    }

    private Customer createCustomer() {
        Customer customer = Customer.builder()
            .name("홍길동")
            .build();
        customerRepository.save(customer);
        return customer;
    }

}