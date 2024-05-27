package com.ourpos.api.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.order.dto.request.DeliveryOrderRequestDto;
import com.ourpos.api.order.dto.request.HallOrderRequestDto;
import com.ourpos.api.order.dto.request.OrderDetailRequestDto;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerRepository;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuRepository;
import com.ourpos.domain.menu.StoreRestrictedMenu;
import com.ourpos.domain.menu.StoreRestrictedMenuRepository;
import com.ourpos.domain.order.DeliveryOrder;
import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.order.Order;
import com.ourpos.domain.order.OrderRepository;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.recipe.Recipe;
import com.ourpos.domain.recipe.RecipeRepository;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;
import com.ourpos.domain.store.StoreStockRepository;
import com.ourpos.domain.storeorder.StoreComm;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    public static final String ORDER_NOT_FOUND = "해당 주문이 존재하지 않습니다.";

    private final OrderRepository<HallOrder> hallOrderRepository;
    private final OrderRepository<DeliveryOrder> deliveryOrderRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final RecipeRepository recipeRepository;
    private final StoreStockRepository storeStockRepository;
    private final StoreRestrictedMenuRepository storeRestrictedMenuRepository;

    @Override
    public void createHallOrder(HallOrderRequestDto hallOrderRequestDto) {
        HallOrder hallOrder = createOrder(hallOrderRequestDto);

        storeStockCalculate(hallOrder);
        hallOrderRepository.save(hallOrder);
    }

    @Override
    public void createDeliveryOrder(DeliveryOrderRequestDto deliveryOrderRequestDto) {
        DeliveryOrder deliveryOrder = createOrder(deliveryOrderRequestDto);
        if (deliveryOrder.getPrice() < deliveryOrder.getStore().getMinimumOrderPrice()) {
            throw new IllegalArgumentException("최소 주문 금액을 충족하지 못했습니다.");
        }

        storeStockCalculate(deliveryOrder);
        deliveryOrderRepository.save(deliveryOrder);
    }

    private void storeStockCalculate(Order order) {
        List<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            try {
                decreaseStoreStock(recipeRepository.findByMenuId(orderDetail.getMenu().getId()), order,
                    orderDetail.getQuantity());
            } catch (IllegalArgumentException e) {
                disabledStoreMenu(orderDetail.getMenu(), order.getStore());
                throw new IllegalArgumentException("재고가 부족합니다.");
            }
        }
    }

    private void disabledStoreMenu(Menu menu, Store store) {
        StoreRestrictedMenu storeRestrictedMenu = StoreRestrictedMenu.builder()
            .store(store)
            .menu(menu)
            .build();

        storeRestrictedMenuRepository.save(storeRestrictedMenu);
    }

    private void decreaseStoreStock(List<Recipe> recipes, Order order, Integer quantity) {
        for (Recipe recipe : recipes) {
            StoreComm storeComm = recipe.getStoreComm();
            storeStockRepository.findByStoreIdAndStoreCommId(order.getStore().getId(), storeComm.getId())
                .forEach(storeStock -> storeStock.decreaseQuantity(recipe.getContent() * quantity));
        }
    }

    @Override
    public void cancelHallOrder(Long orderId) {
        HallOrder order = hallOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.cancelOrder();
    }

    @Override
    public void acceptHallOrder(Long orderId) {
        HallOrder order = hallOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.acceptOrder();
    }

    @Override
    public void completeHallOrder(Long orderId) {
        HallOrder order = hallOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.completeOrder();
    }

    @Override
    public void cancelDeliveryOrder(Long orderId) {
        DeliveryOrder order = deliveryOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.cancelOrder();
    }

    @Override
    public void acceptDeliveryOrder(Long orderId) {
        DeliveryOrder order = deliveryOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.acceptOrder();
    }

    @Override
    public void startDelivery(Long orderId) {
        DeliveryOrder order = deliveryOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.startDelivery();
    }

    @Override
    public void completeDeliveryOrder(Long orderId) {
        DeliveryOrder order = deliveryOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.completeOrder();
    }

    private HallOrder createOrder(HallOrderRequestDto hallOrderRequestDto) {
        Customer customer = getCustomer(hallOrderRequestDto.getCustomerId());
        Store store = getStore(hallOrderRequestDto.getStoreId());

        List<OrderDetail> orderDetails = createOrderDetails(hallOrderRequestDto.getOrderDetailDtos());

        return hallOrderRequestDto.toEntity(customer, store, orderDetails);
    }

    private Customer getCustomer(Long hallOrderRequestDto) {
        return customerRepository.findById(hallOrderRequestDto).orElseThrow(
            () -> new IllegalArgumentException("해당 고객이 존재하지 않습니다."));
    }

    private Store getStore(Long hallOrderRequestDto) {
        return storeRepository.findById(hallOrderRequestDto).orElseThrow(
            () -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));
    }

    private DeliveryOrder createOrder(DeliveryOrderRequestDto deliveryOrderRequestDto) {
        Customer customer = getCustomer(deliveryOrderRequestDto.getCustomerId());
        Store store = getStore(deliveryOrderRequestDto.getStoreId());

        List<OrderDetail> orderDetails = createOrderDetails(
            deliveryOrderRequestDto.getOrderDetailDtos());
        return deliveryOrderRequestDto.toEntity(customer, store, orderDetails);
    }

    private List<OrderDetail> createOrderDetails(List<OrderDetailRequestDto> orderDetailRequestDtos) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailRequestDto orderDetailDto : orderDetailRequestDtos) {
            Menu menu = menuRepository.findById(orderDetailDto.getMenuId()).orElseThrow(
                () -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));
            orderDetails.add(orderDetailDto.toEntity(menu));
        }
        return orderDetails;
    }
}
