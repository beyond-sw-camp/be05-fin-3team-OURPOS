package com.ourpos.api.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.map.MapResponseDto;
import com.ourpos.api.map.MapService;
import com.ourpos.api.order.dto.request.DeliveryOrderRequestDto;
import com.ourpos.api.order.dto.request.HallOrderRequestDto;
import com.ourpos.api.order.dto.request.OrderDetailRequestDto;
import com.ourpos.api.sms.SmsService;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerRepository;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuRepository;
import com.ourpos.domain.menu.StoreRestrictedMenu;
import com.ourpos.domain.menu.StoreRestrictedMenuRepository;
import com.ourpos.domain.order.AdministrativeBuildingAddress;
import com.ourpos.domain.order.AdministrativeBuildingAddressRepository;
import com.ourpos.domain.order.DeliveryOrder;
import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.order.Order;
import com.ourpos.domain.order.OrderAddress;
import com.ourpos.domain.order.OrderRepository;
import com.ourpos.domain.order.TempOrder;
import com.ourpos.domain.order.TempOrderRepository;
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
    private final MapService mapService;
    private final AdministrativeBuildingAddressRepository administrativeBuildingAddressRepository;
    private final TempOrderRepository tempOrderRepository;
    private final SmsService smsService;

    @Override
    public Long createHallOrder(String loginId, HallOrderRequestDto hallOrderRequestDto) {
        HallOrder hallOrder = createOrder(loginId, hallOrderRequestDto);

        storeStockCalculate(hallOrder);
        HallOrder savedHallOrder = hallOrderRepository.save(hallOrder);

        return savedHallOrder.getId();
    }

    @Override
    public void createDeliveryOrder(String loginId, DeliveryOrderRequestDto deliveryOrderRequestDto,
        String tempOrderId) {

        DeliveryOrder deliveryOrder = createOrder(loginId, deliveryOrderRequestDto);
        if (deliveryOrder.getPrice() < deliveryOrder.getStore().getMinimumOrderPrice()) {
            throw new IllegalArgumentException("최소 주문 금액을 충족하지 못했습니다.");
        }

        TempOrder tempOrder = tempOrderRepository.findByTempOrderId(tempOrderId).orElseThrow(
            () -> new IllegalArgumentException("해당 임시 주문이 존재하지 않습니다."));

        if (deliveryOrder.getPrice() != tempOrder.getAmount()) {
            throw new IllegalArgumentException("주문 금액이 일치하지 않습니다.");
        }

        storeStockCalculate(deliveryOrder);
        deliveryOrderRepository.save(deliveryOrder);

        addAdministrativeBuildingAddress(deliveryOrder);
    }

    private void addAdministrativeBuildingAddress(DeliveryOrder deliveryOrder) {
        OrderAddress orderAddress = deliveryOrder.getOrderAddress();
        List<MapResponseDto> mapResponseDtos = mapService.searchAddress(
            orderAddress.getAddressBase());

        for (MapResponseDto mapResponseDto : mapResponseDtos) {
            if (mapResponseDto.getRegion_type().equals("H")) {
                String code = mapResponseDto.getCode();
                AdministrativeBuildingAddress administrativeBuildingAddress = administrativeBuildingAddressRepository.findById(
                    code).orElseThrow(() -> new IllegalArgumentException("해당 건물이 존재하지 않습니다."));
                orderAddress.addAdministrativeBuildingAddress(administrativeBuildingAddress);
            }
        }
    }

    private void storeStockCalculate(Order order) {
        List<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            try {
                decreaseStoreStock(recipeRepository.findByMenuId(orderDetail.getMenu().getId()), order.getStore(),
                    orderDetail.getQuantity());
            } catch (IllegalArgumentException e) {
                disabledStoreMenu(orderDetail.getMenu(), order.getStore());
                throw new IllegalArgumentException("재고가 부족합니다.");
            }
        }
    }

    //메뉴 비활성화
    private void disabledStoreMenu(Menu menu, Store store) {
        StoreRestrictedMenu storeRestrictedMenu = StoreRestrictedMenu.builder()
            .store(store)
            .menu(menu)
            .build();

        storeRestrictedMenuRepository.save(storeRestrictedMenu);
    }

    private void decreaseStoreStock(List<Recipe> recipes, Store store, Integer quantity) {
        for (Recipe recipe : recipes) {
            StoreComm storeComm = recipe.getStoreComm();
            storeStockRepository.findByStoreIdAndStoreCommId(store.getId(), storeComm.getId())
                .forEach(storeStock -> storeStock.decreaseQuantity(recipe.getContent() * quantity));
        }
    }

    //비활성화 된 메뉴 재활성화
    /*
    public void enableMenu(Menu menu, Store store) {
        List<StoreRestrictedMenu> restrictedMenus = storeRestrictedMenuRepository.findByStore(store);
        StoreRestrictedMenu restrictedMenuToRemove = null;
        for (StoreRestrictedMenu restrictedMenu : restrictedMenus) {
            if (restrictedMenu.getMenu().equals(menu)) {
                restrictedMenuToRemove = restrictedMenu;
                break;
            }
        }
        if (restrictedMenuToRemove != null) {
            storeRestrictedMenuRepository.delete(restrictedMenuToRemove);
        }
    }

     */

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

        order.completeOrder(LocalDateTime.now());
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

        order.completeOrder(LocalDateTime.now());
    }

    @Override
    public TempOrder saveTempOrder(String tempOrderId, int amount) {

        TempOrder tempOrder = TempOrder.builder()
            .tempOrderId(tempOrderId)
            .amount(amount)
            .isConfirmed(false)
            .build();

        return tempOrderRepository.save(tempOrder);
    }

    @Override
    public void deleteTempOrder(String orderId) {
        TempOrder tempOrder = tempOrderRepository.findByTempOrderId(orderId).orElseThrow(
            () -> new IllegalArgumentException("해당 임시 주문이 존재하지 않습니다."));

        tempOrderRepository.delete(tempOrder);
    }

    private HallOrder createOrder(String loginId, HallOrderRequestDto hallOrderRequestDto) {
        Customer customer = getCustomer(loginId);
        Store store = getStore(hallOrderRequestDto.getStoreId());
        List<OrderDetail> orderDetails = createOrderDetails(hallOrderRequestDto.getOrderDetailDtos());

        HallOrder order = hallOrderRequestDto.toEntity(customer, store, orderDetails);

        if (customer.getPhone() != null) {
            smsService.sendOne(customer.getPhone(), "[OURPOS]\n" + store.getName() + " 홀/포장 주문이 완료되었습니다.\n\n"
                + "주문 상품: " + orderDetails.get(0).getMenu().getName() + " 외 " + (orderDetails.size() - 1) + "개\n"
                + "총 가격: " + order.getPrice() + "원\n");
        }

        return order;
    }

    private DeliveryOrder createOrder(String loginId, DeliveryOrderRequestDto deliveryOrderRequestDto) {
        Customer customer = getCustomer(loginId);
        Store store = getStore(deliveryOrderRequestDto.getStoreId());

        List<OrderDetail> orderDetails = createOrderDetails(
            deliveryOrderRequestDto.getOrderDetailDtos());

        return deliveryOrderRequestDto.toEntity(customer, store, orderDetails);
    }

    private Customer getCustomer(String loginId) {
        return customerRepository.findByLoginId(loginId).orElseThrow(
            () -> new IllegalArgumentException("해당 고객이 존재하지 않습니다."));
    }

    private Store getStore(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(
            () -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));
    }

    List<OrderDetail> createOrderDetails(List<OrderDetailRequestDto> orderDetailRequestDtos) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailRequestDto orderDetailDto : orderDetailRequestDtos) {
            Menu menu = menuRepository.findById(orderDetailDto.getMenuId()).orElseThrow(
                () -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));
            orderDetails.add(orderDetailDto.toEntity(menu));
        }
        return orderDetails;
    }
}
