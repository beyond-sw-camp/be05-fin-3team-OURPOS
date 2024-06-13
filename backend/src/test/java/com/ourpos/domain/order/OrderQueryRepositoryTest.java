package com.ourpos.domain.order;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerRepository;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;

@Transactional
@SpringBootTest
class OrderQueryRepositoryTest {

    @Autowired
    private OrderQueryRepository orderQueryRepository;

    @Autowired
    private OrderRepository<HallOrder> hallOrderRepository;

    @Autowired
    private OrderRepository<DeliveryOrder> deliveryOrderRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1;
    private Customer customer2;
    private Store store1;
    private Store store2;

    @BeforeEach
    void setUp() {
        // fixture
        customer1 = Customer.builder()
            .name("홍길동")
            .loginId("naver_123")
            .build();
        customer2 = Customer.builder()
            .name("김철수")
            .loginId("kakao_456")
            .build();
        customerRepository.saveAll(List.of(customer1, customer2));

        store1 = Store.builder()
            .name("강남점")
            .build();
        store2 = Store.builder()
            .name("홍대점")
            .build();

        storeRepository.saveAll(List.of(store1, store2));
    }

    @DisplayName("특정 가게의 홀/포장 주문을 주문 상태에 따라 조회한다.")
    @Test
    void findHallOrders() {
        // given
        HallOrder waitingHallOrder1 = createHallOrder(true, customer1, store1);
        HallOrder waitingHallOrder2 = createHallOrder(false, customer1, store1);
        HallOrder cookingHallOrder = createHallOrder(true, customer1, store1);
        cookingHallOrder.acceptOrder();

        hallOrderRepository.saveAll(List.of(waitingHallOrder1, waitingHallOrder2));

        Pageable pageable = PageRequest.of(0, 3);

        // when
        Page<HallOrder> hallOrders = orderQueryRepository.findHallOrders(store1.getId(),
            String.valueOf(HallStatus.WAITING), pageable);
        List<HallOrder> content = hallOrders.getContent();

        // then
        assertThat(content).hasSize(2)
            .extracting("orderTakeoutYn")
            .containsExactlyInAnyOrder(true, false);
    }

    @DisplayName("특정 가게의 홀/포장 주문을 조회할 때 주문 상태가 완료인 경우 3일 이내의 주문만 조회한다.")
    @Test
    void findHallOrdersCompleted() {
        // given
        HallOrder completedHallOrder1 = createHallOrder(true, customer1, store1);
        HallOrder completedHallOrder2 = createHallOrder(false, customer1, store1);

        LocalDateTime now = LocalDateTime.now();
        completedHallOrder1.acceptOrder();
        completedHallOrder1.completeOrder(now);

        completedHallOrder2.acceptOrder();
        completedHallOrder2.completeOrder(now.minusDays(4));

        hallOrderRepository.saveAll(List.of(completedHallOrder1, completedHallOrder2));

        Pageable pageable = PageRequest.of(0, 3);

        // when
        Page<HallOrder> hallOrders = orderQueryRepository.findHallOrders(store1.getId(),
            String.valueOf(HallStatus.COMPLETED), pageable);
        List<HallOrder> content = hallOrders.getContent();

        // then
        assertThat(content).hasSize(1)
            .extracting("orderTakeoutYn")
            .containsExactly(true);
    }

    @DisplayName("특정 가게의 홀/포장 주문을 조회할 때 주문 상태가 null인 경우 전체 주문을 조회한다.")
    @Test
    void findHallOrdersAll() {
        // given
        HallOrder waitingHallOrder1 = createHallOrder(true, customer1, store1);
        HallOrder waitingHallOrder2 = createHallOrder(false, customer1, store1);
        HallOrder cookingHallOrder = createHallOrder(true, customer1, store1);
        cookingHallOrder.acceptOrder();

        hallOrderRepository.saveAll(List.of(waitingHallOrder1, waitingHallOrder2, cookingHallOrder));

        Pageable pageable = PageRequest.of(0, 3);

        // when
        String status = null;
        Page<HallOrder> hallOrders = orderQueryRepository.findHallOrders(store1.getId(), status, pageable);
        List<HallOrder> content = hallOrders.getContent();

        // then
        assertThat(content).hasSize(3)
            .extracting("orderTakeoutYn")
            .containsExactly(true, false, true);
    }

    @DisplayName("홀 주문을 조회하는 경우 페이징 처리가 가능하다. 기본 페이지 크기는 10이다.")
    @Test
    void findHallOrdersWithPaging() {
        // given
        for (int i = 0; i < 15; i++) {
            HallOrder hallOrder = createHallOrder(true, customer1, store1);
            hallOrderRepository.save(hallOrder);
        }

        Pageable pageable = PageRequest.of(1, 10);

        // when
        Page<HallOrder> hallOrders = orderQueryRepository.findHallOrders(store1.getId(),
            String.valueOf(HallStatus.WAITING), pageable);
        List<HallOrder> content = hallOrders.getContent();

        // then
        assertThat(content).hasSize(5);
    }

    @DisplayName("특정 가게의 특정 홀 주문을 주문 id를 통해 조회한다.")
    @Test
    void findOneHallOrder() {
        // given
        HallOrder hallOrder = createHallOrder(true, customer1, store1);
        hallOrderRepository.save(hallOrder);

        // when
        HallOrder findHallOrder = orderQueryRepository.findOneHallOrder(hallOrder.getId()).orElseThrow();

        // then
        assertThat(findHallOrder.getId()).isEqualTo(hallOrder.getId());
    }

    @DisplayName("특정 가게의 배달 주문을 주문 id를 통해 조회한다.")
    @Test
    void findOneDeliveryOrder() {
        // given
        DeliveryOrder deliveryOrder = createDeliveryOrder(customer1, store1);
        deliveryOrderRepository.save(deliveryOrder);

        // when
        DeliveryOrder findDeliveryOrder = orderQueryRepository.findOneDeliveryOrder(deliveryOrder.getId())
            .orElseThrow();

        // then
        assertThat(findDeliveryOrder.getId()).isEqualTo(deliveryOrder.getId());
    }

    @DisplayName("특정 가게의 배달 주문을 주문 상태에 따라 조회한다.")
    @Test
    void findDeliveryOrders() {
        // given
        DeliveryOrder waitingDeliveryOrder1 = createDeliveryOrder(customer1, store1);
        DeliveryOrder waitingDeliveryOrder2 = createDeliveryOrder(customer1, store1);
        DeliveryOrder cookingDeliveryOrder = createDeliveryOrder(customer1, store1);
        cookingDeliveryOrder.acceptOrder();

        deliveryOrderRepository.saveAll(List.of(waitingDeliveryOrder1, waitingDeliveryOrder2, cookingDeliveryOrder));

        Pageable pageable = PageRequest.of(0, 3);

        // when
        Page<DeliveryOrder> deliveryOrders = orderQueryRepository.findDeliveryOrders(store1.getId(),
            String.valueOf(DeliveryStatus.WAITING), pageable);
        List<DeliveryOrder> content = deliveryOrders.getContent();

        // then
        assertThat(content).hasSize(2)
            .extracting("orderAddress")
            .containsExactlyInAnyOrder(waitingDeliveryOrder1.getOrderAddress(),
                waitingDeliveryOrder2.getOrderAddress());
    }

    @DisplayName("특정 가게의 배달 주문을 조회할 때 주문 상태가 완료인 경우 3일 이내의 주문만 조회한다.")
    @Test
    void findDeliveryOrdersCompleted() {
        // given
        DeliveryOrder completedDeliveryOrder1 = createDeliveryOrder(customer1, store1);
        DeliveryOrder completedDeliveryOrder2 = createDeliveryOrder(customer1, store1);

        LocalDateTime now = LocalDateTime.now();
        completedDeliveryOrder1.acceptOrder();
        completedDeliveryOrder1.startDelivery();
        completedDeliveryOrder1.completeOrder(now);

        completedDeliveryOrder2.acceptOrder();
        completedDeliveryOrder2.startDelivery();
        completedDeliveryOrder2.completeOrder(now.minusDays(4));

        deliveryOrderRepository.saveAll(List.of(completedDeliveryOrder1, completedDeliveryOrder2));

        Pageable pageable = PageRequest.of(0, 3);

        // when
        Page<DeliveryOrder> deliveryOrders = orderQueryRepository.findDeliveryOrders(store1.getId(),
            String.valueOf(DeliveryStatus.COMPLETED), pageable);
        List<DeliveryOrder> content = deliveryOrders.getContent();

        // then
        assertThat(content).hasSize(1)
            .extracting("orderAddress")
            .containsExactly(completedDeliveryOrder1.getOrderAddress());
    }

    @DisplayName("특정 가게의 배달 주문을 조회할 때 주문 상태가 null인 경우 전체 주문을 조회한다.")
    @Test
    void findDeliveryOrdersAll() {
        // given
        DeliveryOrder waitingDeliveryOrder1 = createDeliveryOrder(customer1, store1);
        DeliveryOrder waitingDeliveryOrder2 = createDeliveryOrder(customer1, store1);
        DeliveryOrder cookingDeliveryOrder = createDeliveryOrder(customer1, store1);
        cookingDeliveryOrder.acceptOrder();

        deliveryOrderRepository.saveAll(List.of(waitingDeliveryOrder1, waitingDeliveryOrder2, cookingDeliveryOrder));

        Pageable pageable = PageRequest.of(0, 3);

        // when
        String status = null;
        Page<DeliveryOrder> deliveryOrders = orderQueryRepository.findDeliveryOrders(store1.getId(), status, pageable);
        List<DeliveryOrder> content = deliveryOrders.getContent();

        // then
        assertThat(content).hasSize(3)
            .extracting("orderAddress")
            .containsExactlyInAnyOrder(waitingDeliveryOrder1.getOrderAddress(), waitingDeliveryOrder2.getOrderAddress(),
                cookingDeliveryOrder.getOrderAddress());
    }

    @DisplayName("배달 주문을 조회하는 경우 페이징 처리가 가능하다. 기본 페이지 크기는 10이다.")
    @Test
    void findDeliveryOrdersWithPaging() {
        // given
        for (int i = 0; i < 15; i++) {
            DeliveryOrder deliveryOrder = createDeliveryOrder(customer1, store1);
            deliveryOrderRepository.save(deliveryOrder);
        }

        Pageable pageable = PageRequest.of(1, 10);

        // when
        Page<DeliveryOrder> deliveryOrders = orderQueryRepository.findDeliveryOrders(store1.getId(),
            String.valueOf(DeliveryStatus.WAITING), pageable);
        List<DeliveryOrder> content = deliveryOrders.getContent();

        // then
        assertThat(content).hasSize(5);
    }

    @DisplayName("특정 고객의 홀 주문을 조회한다.")
    @Test
    void findHallOrdersByLoginId() {
        // given
        HallOrder hallOrder1 = createHallOrder(true, customer1, store1);
        HallOrder hallOrder2 = createHallOrder(false, customer1, store1);
        HallOrder hallOrder3 = createHallOrder(true, customer2, store2);

        hallOrderRepository.saveAll(List.of(hallOrder1, hallOrder2, hallOrder3));

        Pageable pageable = PageRequest.of(0, 3);

        // when
        Page<HallOrder> hallOrders = orderQueryRepository.findHallOrdersByLoginId(customer1.getLoginId(), pageable);
        List<HallOrder> content = hallOrders.getContent();

        // then
        assertThat(content).hasSize(2)
            .extracting("customer", "orderTakeoutYn")
            .containsExactlyInAnyOrder(
                tuple(customer1, true),
                tuple(customer1, false)
            );
    }

    @DisplayName("특정 고객의 홀 주문을 조회할 때 페이징 처리가 가능하다. 기본 페이지 크기는 10이다.")
    @Test
    void findHallOrdersByLoginIdWithPaging() {
        // given
        for (int i = 0; i < 15; i++) {
            HallOrder hallOrder = createHallOrder(true, customer1, store1);
            hallOrderRepository.save(hallOrder);
        }

        Pageable pageable = PageRequest.of(1, 10);

        // when
        Page<HallOrder> hallOrders = orderQueryRepository.findHallOrdersByLoginId(customer1.getLoginId(), pageable);
        List<HallOrder> content = hallOrders.getContent();

        // then
        assertThat(content).hasSize(5);
    }

    @DisplayName("특정 고객의 배달 주문을 조회할 때 주문 상태가 null인 경우 전체 주문을 조회한다.")
    @Test
    void findDeliveryOrdersByLoginIdAll() {
        // given
        DeliveryOrder waitingDeliveryOrder1 = createDeliveryOrder(customer1, store1);
        DeliveryOrder waitingDeliveryOrder2 = createDeliveryOrder(customer1, store1);
        DeliveryOrder cookingDeliveryOrder = createDeliveryOrder(customer1, store2);
        cookingDeliveryOrder.acceptOrder();

        deliveryOrderRepository.saveAll(List.of(waitingDeliveryOrder1, waitingDeliveryOrder2, cookingDeliveryOrder));

        Pageable pageable = PageRequest.of(0, 3);

        // when
        Page<DeliveryOrder> deliveryOrders = orderQueryRepository.findDeliveryOrdersByLoginId(customer1.getLoginId(),
            null, pageable);
        List<DeliveryOrder> content = deliveryOrders.getContent();

        // then
        assertThat(content).hasSize(3)
            .extracting("store")
            .containsExactlyInAnyOrder(store1, store1, store2);
    }

    @DisplayName("특정 고객의 배달 주문을 주문 상태에 따라 조회한다.")
    @Test
    void findDeliveryOrdersByLoginIdAndStatus() {
        // given
        DeliveryOrder waitingDeliveryOrder1 = createDeliveryOrder(customer1, store1);
        DeliveryOrder waitingDeliveryOrder2 = createDeliveryOrder(customer1, store1);
        DeliveryOrder cookingDeliveryOrder = createDeliveryOrder(customer1, store2);
        cookingDeliveryOrder.acceptOrder();

        deliveryOrderRepository.saveAll(List.of(waitingDeliveryOrder1, waitingDeliveryOrder2, cookingDeliveryOrder));

        Pageable pageable = PageRequest.of(0, 3);

        // when
        Page<DeliveryOrder> deliveryOrders = orderQueryRepository.findDeliveryOrdersByLoginId(customer1.getLoginId(),
            String.valueOf(DeliveryStatus.COOKING), pageable);
        List<DeliveryOrder> content = deliveryOrders.getContent();

        // then
        assertThat(content).hasSize(1)
            .extracting("store")
            .containsExactly(store2);
    }

    @DisplayName("특정 고객의 배달 주문을 조회하면 최근에 주문한 순서대로 정렬된다.")
    @Test
    void findDeliveryOrdersByLoginIdOrderByCreatedDateTimeDesc() {
        // given
        DeliveryOrder firstOrder = createDeliveryOrder(customer1, store1);
        DeliveryOrder secondOrder = createDeliveryOrder(customer1, store2);

        deliveryOrderRepository.saveAll(List.of(firstOrder, secondOrder));

        Pageable pageable = PageRequest.of(0, 3);

        // when
        Page<DeliveryOrder> deliveryOrders = orderQueryRepository.findDeliveryOrdersByLoginId(customer1.getLoginId(),
            null, pageable);
        List<DeliveryOrder> content = deliveryOrders.getContent();

        // then
        LocalDateTime firstOrderDateTime = content.get(0).getCreatedDateTime();
        LocalDateTime secondOrderDateTime = content.get(1).getCreatedDateTime();

        assertThat(firstOrderDateTime).isAfter(secondOrderDateTime);
        assertThat(content).hasSize(2)
            .extracting("store")
            .containsExactly(secondOrder.getStore(), firstOrder.getStore());
    }

    private HallOrder createHallOrder(boolean orderTakeoutYn, Customer customer, Store store) {
        return HallOrder.builder()
            .customer(customer)
            .store(store)
            .orderTakeoutYn(orderTakeoutYn)
            .build();
    }

    private DeliveryOrder createDeliveryOrder(Customer customer, Store store) {
        return DeliveryOrder.builder()
            .customer(customer)
            .store(store)
            .orderAddress(createOrderAddress())
            .build();
    }

    private OrderAddress createOrderAddress() {
        return OrderAddress.builder()
            .addressBase("서울시 강남구")
            .addressDetail("강남역 1번출구")
            .zipcode("12345")
            .build();
    }

}