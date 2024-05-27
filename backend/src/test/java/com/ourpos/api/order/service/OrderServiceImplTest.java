package com.ourpos.api.order.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import com.ourpos.domain.recipe.Recipe;
import com.ourpos.domain.recipe.RecipeRepository;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;
import com.ourpos.domain.store.StoreStock;
import com.ourpos.domain.store.StoreStockRepository;
import com.ourpos.domain.storeorder.StoreComm;
import com.ourpos.domain.storeorder.StoreCommCategory;
import com.ourpos.domain.storeorder.StoreCommRepository;

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
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private StoreCommRepository storeCommRepository;
    @Autowired
    private StoreStockRepository storeStockRepository;

    @DisplayName("포장/홀 주문을 통해 주문 하게 되면 레시피만큼 재고가 감소한다.")
    @Test
    void decreaseStoreStock() {
        // given
        Customer customer = createCustomer();
        Store store = createStore();

        Menu hamburger = createMenu("햄버거", 5000);
        Menu coke = createMenu("콜라", 1000);

        StoreComm pt = createStoreComm();
        createRecipe(pt, hamburger, 2);
        createRecipe(pt, coke, 1);
        StoreStock storeStock = createStoreStock(store, pt, 10);

        OrderOptionRequestDto orderOption1 = createOrderOption();
        OrderOptionRequestDto orderOption2 = createOrderOption();
        OrderOptionGroupRequestDto orderOptionGroup1 = createOrderOptionGroup(List.of(orderOption1, orderOption2));
        OrderOptionGroupRequestDto orderOptionGroup2 = createOrderOptionGroup(List.of(orderOption2));

        OrderDetailRequestDto orderDetail1 = createOrderDetail(hamburger, 2,
            List.of(orderOptionGroup1, orderOptionGroup2));
        OrderDetailRequestDto orderDetail2 = createOrderDetail(coke, 1,
            List.of(orderOptionGroup1, orderOptionGroup2));

        HallOrderRequestDto hallOrder = createHallOrder(customer, store, List.of(orderDetail1, orderDetail2));

        // when
        orderServiceImpl.createHallOrder(hallOrder);

        // then
        assertThat(storeStock.getQuantity()).isEqualTo(5);
    }

    @DisplayName("재고가 모두 소진되면 주문이 불가능하다.")
    @Test
    void soldOut() {
        // given
        Customer customer = createCustomer();
        Store store = createStore();

        Menu hamburger = createMenu("햄버거", 5000);
        Menu coke = createMenu("콜라", 1000);

        StoreComm pt = createStoreComm();
        createRecipe(pt, hamburger, 2);
        createRecipe(pt, coke, 1);
        StoreStock storeStock = createStoreStock(store, pt, 1);

        OrderOptionRequestDto orderOption1 = createOrderOption();
        OrderOptionRequestDto orderOption2 = createOrderOption();
        OrderOptionGroupRequestDto orderOptionGroup1 = createOrderOptionGroup(List.of(orderOption1, orderOption2));
        OrderOptionGroupRequestDto orderOptionGroup2 = createOrderOptionGroup(List.of(orderOption2));

        OrderDetailRequestDto orderDetail1 = createOrderDetail(hamburger, 2,
            List.of(orderOptionGroup1, orderOptionGroup2));
        OrderDetailRequestDto orderDetail2 = createOrderDetail(coke, 1,
            List.of(orderOptionGroup1, orderOptionGroup2));

        HallOrderRequestDto hallOrder = createHallOrder(customer, store, List.of(orderDetail1, orderDetail2));

        // when
        assertThat(hamburger.getAvailableYn()).isTrue();
        assertThatThrownBy(() -> orderServiceImpl.createHallOrder(hallOrder))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("재고가 부족합니다.");
        assertThat(hamburger.getAvailableYn()).isFalse();
    }

    private StoreComm createStoreComm() {
        StoreComm storeComm = StoreComm.builder()
            .name("패티")
            .articleUnit("개")
            .category(StoreCommCategory.INGREDIENT)
            .price(1000)
            .build();
        storeCommRepository.save(storeComm);
        return storeComm;
    }

    private Recipe createRecipe(StoreComm storeComm, Menu hamburger, int content) {
        Recipe recipe = Recipe.builder()
            .storeComm(storeComm)
            .menu(hamburger)
            .content(content)
            .build();
        recipeRepository.save(recipe);
        return recipe;
    }

    private StoreStock createStoreStock(Store store, StoreComm storeComm, int quantity) {
        StoreStock storeStock = StoreStock.builder()
            .store(store)
            .storeComm(storeComm)
            .quantity(quantity)
            .build();
        storeStockRepository.save(storeStock);

        return storeStock;
    }

    private static HallOrderRequestDto createHallOrder(Customer customer, Store store,
        List<OrderDetailRequestDto> orderDetailRequestDto) {
        HallOrderRequestDto hallOrderRequestDto = new HallOrderRequestDto();
        hallOrderRequestDto.setCustomerId(customer.getId());
        hallOrderRequestDto.setStoreId(store.getId());
        hallOrderRequestDto.setOrderTakeoutYn(false);
        hallOrderRequestDto.setOrderDetailDtos(orderDetailRequestDto);
        return hallOrderRequestDto;
    }

    private static OrderDetailRequestDto createOrderDetail(Menu menu, int quantity,
        List<OrderOptionGroupRequestDto> orderOptionGroupRequestDto) {
        OrderDetailRequestDto orderDetailRequestDto = new OrderDetailRequestDto();
        orderDetailRequestDto.setMenuId(menu.getId());
        orderDetailRequestDto.setQuantity(quantity);
        orderDetailRequestDto.setOrderOptionGroups(orderOptionGroupRequestDto);
        return orderDetailRequestDto;
    }

    private static OrderOptionGroupRequestDto createOrderOptionGroup(
        List<OrderOptionRequestDto> orderOptionRequestDto) {
        OrderOptionGroupRequestDto orderOptionGroupRequestDto = new OrderOptionGroupRequestDto();
        orderOptionGroupRequestDto.setOptionGroupName("사이드 선택");
        orderOptionGroupRequestDto.setOrderOptions(orderOptionRequestDto);
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