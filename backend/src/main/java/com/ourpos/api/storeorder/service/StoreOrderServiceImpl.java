package com.ourpos.api.storeorder.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.store.service.StoreStockServiceImpl;
import com.ourpos.api.storeorder.dto.request.StoreOrderRequestDto;
import com.ourpos.api.storeorder.dto.response.StoreCommResponseDto;
import com.ourpos.api.storeorder.dto.response.StoreOrderCheckResponseDto;
import com.ourpos.api.storeorder.dto.response.StoreOrderResponseDto;
import com.ourpos.auth.dto.manager.ManagerUserDetails;
import com.ourpos.domain.recipe.RecipeRepository;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;
import com.ourpos.domain.store.StoreStockRepository;
import com.ourpos.domain.storeorder.StoreComm;
import com.ourpos.domain.storeorder.StoreCommCategory;
import com.ourpos.domain.storeorder.StoreCommRepository;
import com.ourpos.domain.storeorder.StoreOrder;
import com.ourpos.domain.storeorder.StoreOrderDetail;
import com.ourpos.domain.storeorder.StoreOrderDetailRepository;
import com.ourpos.domain.storeorder.StoreOrderRepository;
import com.ourpos.domain.storeorder.StoreOrderStatus;
import java.util.List;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
@Transactional
@Service
public class StoreOrderServiceImpl {
    private final StoreOrderRepository storeOrderRepository;
    private final StoreOrderDetailRepository storeOrderDetailRepository;
    private final StoreCommRepository storeCommRepository;
    //private final StoreComm storeComm;
    private final StoreRepository storeRepository;
    private final StoreStockRepository storeStockRepository;
    private final RecipeRepository recipeRepository;
    private final StoreStockServiceImpl storeStockService;

    /*//판매 비품, 식자재 목록 확인 페이징 처리 x, 로그인 x
     * public List<StoreCommResponseDto> checkStoreComms() {
        System.out.println("StoreCommServiceImplServiceImpl.getStoreComms");
        List<StoreComm> storeComms = storeCommRepository.findAll();
        List<StoreCommResponseDto> storeCommResponseDtos = new ArrayList<>();

        for (StoreComm storeComm : storeComms) {
            storeCommResponseDtos.add(new StoreCommResponseDto(storeComm));
        }
        return storeCommResponseDtos;
    }
     */
    // 판매 비품, 식자재 목록 확인 (페이징 처리 o)
    /* 
    public Page<StoreCommResponseDto> checkStoreComms(Pageable pageable) {
        Page<StoreComm> storeCommPage = storeCommRepository.findAll(pageable);
        List<StoreCommResponseDto> storeCommResponseDtos = storeCommPage.stream()
            .map(StoreCommResponseDto::new)
            .collect(Collectors.toList());
        return new PageImpl<>(storeCommResponseDtos, pageable, storeCommPage.getTotalElements());
    }
    */
    
    // 판매 비품, 식자재 목록 확인2 (페이징 처리 o)
    public Page<StoreComm> getStoreCommsByCategory(StoreCommCategory category, Pageable pageable) {
    return storeCommRepository.findByCategory(category, pageable);
}
    // 비품, 식자재 주문 (비품,식자재 주문 관리에서 배달완료 시 재고에 반영)
    public void createStoreOrder(String adminLoginId, StoreOrderRequestDto requestDto) {

        Store store = storeRepository.findByManagerLoginId(adminLoginId)
            .orElseThrow(() -> new IllegalArgumentException("해당 상점을 찾을 수 없습니다."));

        // StoreOrder 생성
        StoreOrder storeOrder = StoreOrder.builder()
            .price(requestDto.getStoreCommPrice())
            .store(store)
            .quantity(requestDto.getStoreOrderDetailQuantity())
            .build();

        // StoreOrder 저장
        storeOrderRepository.save(storeOrder);

        StoreComm storeComm = storeCommRepository.findById(requestDto.getStoreCommId())
            .orElseThrow(() -> new IllegalArgumentException("해당 StoreComm을 찾을 수 없습니다."));

        // StoreOrderDetail 생성
        StoreOrderDetail storeOrderDetail = StoreOrderDetail.builder()
            .storeOrder(storeOrder)
            .storeMenu(storeComm)
            .build();

        // StoreOrderDetail 저장
        storeOrderDetailRepository.save(storeOrderDetail);
    }

    // 비품, 식자재 주문 내역 확인(직영점)
    public StoreOrderResponseDto getStoreOrder(Long orderDetailId) {
        StoreOrderDetail storeOrderDetail = storeOrderDetailRepository.findById(orderDetailId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        return new StoreOrderResponseDto(storeOrderDetail);

    }

    // 비품, 식자재 주문 수정
    public void updateStoreOrder(Long orderDetailId, StoreOrderRequestDto storeOrderResponseDto) {
        StoreOrderDetail storeOrderDetail = storeOrderDetailRepository.findById(orderDetailId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        // storeOrderDetail.update(storeOrderResponseDto.getStoreOrderDetailQuantity());
        storeOrderDetailRepository.save(storeOrderDetail);
    }

    // 비품, 식자재 주문 삭제
    public void deleteStoreOrder(Long orderDetailId) {
        StoreOrderDetail storeOrderDetail = storeOrderDetailRepository.findById(orderDetailId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        storeOrderDetailRepository.delete(storeOrderDetail);

    }

    //비품, 식자재 주문 확인(본사)
    /* 
    public Page<StoreOrderCheckResponseDto> getStoreOrdercheck(Long storeId, int pageNumber, int pageSize) {
        System.out.println("StoreOrderService.getStoreOrdercheck");

        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new IllegalArgumentException("해당 상점을 찾을 수 없습니다."));
        List<StoreOrder> storeOrders = storeOrderRepository.findByStoreId(storeId);

        //Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        //store의 주문을 페이지로 조회
        List<StoreOrderStatus> statuses = Arrays.asList(
            StoreOrderStatus.WAITING
        );
        Page<StoreOrder> storeOrderPage = storeOrderRepository.findByStoreIdAndStatusIn(storeId, statuses, pageable);

		if (storeOrders.isEmpty()) {
			throw new IllegalArgumentException("해당 상점의 주문을 찾을 수 없습니다.");
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");

        //페이지의 주문 상세를 DTO로 매핑해서 반환
        List<StoreOrderCheckResponseDto> orderDetails = storeOrderPage.getContent().stream()
            .flatMap(storeOrder -> {
                List<StoreOrderDetail> storeOrderDetails = storeOrderDetailRepository.findByStoreOrderId(
                    storeOrder.getId());

			return storeOrderDetails.stream()
			       .map(detail -> new StoreOrderCheckResponseDto(
							storeOrder.getId(),
							storeOrder.getCreatedDateTime().format(formatter), 
                            storeOrder.getPrice(),
                            storeOrder.getStatus(),
                            store.getAddress().getAddressBase(),
                            store.getAddress().getAddressDetail(),
                            store.getAddress().getZipcode(),
                            store.getName(),
                            store.getPhone(),
                            storeId,
                            detail.getStoreMenu().getName(),
                            storeOrder.getPrice(),
                            detail.getStoreMenu().getArticleUnit(),
                            detail.getStoreMenu().getPictureUrl(),
                            storeOrder.getQuantity(),
                            detail.getStoreMenu().getPrice()

                    ));
            })

				   .collect(Collectors.toList());
				   return new PageImpl<>(orderDetails, pageable, storeOrderPage.getTotalElements());

	}
                   */
    //대기중
    public Page<StoreOrderCheckResponseDto> getStoreOrdercheckw(int pageNumber, int pageSize) {
        System.out.println("StoreOrderService.getStoreOrdercheckw");

        // 최신 주문 순으로 정렬된 Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDateTime"));
         // ACCEPTED 상태인 주문을 조회할 상태 리스트 생성
        List<StoreOrderStatus> statuses = Arrays.asList(StoreOrderStatus.WAITING);
        // 상태가 ACCEPTED인 모든 주문을 페이지로 조회
        Page<StoreOrder> storeOrderPage = storeOrderRepository.findByStatusIn(statuses, pageable);
        if (storeOrderPage.isEmpty()) {
            throw new IllegalArgumentException("주문을 찾을 수 없습니다.");
        }

        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        //페이지의 주문 상세를 DTO로 매핑해서 반환
        List<StoreOrderCheckResponseDto> orderDetails = storeOrderPage.getContent().stream()
            .flatMap(storeOrder -> {
                List<StoreOrderDetail> storeOrderDetails = storeOrderDetailRepository.findByStoreOrderId(
                    storeOrder.getId());

			return storeOrderDetails.stream()
			       .map(detail -> new StoreOrderCheckResponseDto(
							storeOrder.getId(),
							storeOrder.getCreatedDateTime().format(formatter), 
                            storeOrder.getPrice(),
                            storeOrder.getStatus(),
                            storeOrder.getStore().getAddress().getAddressBase(),
                            storeOrder.getStore().getAddress().getAddressDetail(),
                            storeOrder.getStore().getAddress().getZipcode(),
                            storeOrder.getStore().getName(),
                            storeOrder.getStore().getPhone(),
                            storeOrder.getStore().getId(),
                            detail.getStoreMenu().getName(),
                            storeOrder.getPrice(),
                            detail.getStoreMenu().getArticleUnit(),
                            detail.getStoreMenu().getPictureUrl(),
                            storeOrder.getQuantity(),
                            detail.getStoreMenu().getPrice()

                    ));
            })

				   .collect(Collectors.toList());
				   return new PageImpl<>(orderDetails, pageable, storeOrderPage.getTotalElements());

	}

	 
	//주문 승인
    public Page<StoreOrderCheckResponseDto> getStoreOrderchecka(int pageNumber, int pageSize) {
        System.out.println("StoreOrderService.getStoreOrdercheckw");
        //Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
         // ACCEPTED 상태인 주문을 조회할 상태 리스트 생성
        List<StoreOrderStatus> statuses = Arrays.asList(StoreOrderStatus.ACCEPTED);
        // 상태가 ACCEPTED인 모든 주문을 페이지로 조회
        Page<StoreOrder> storeOrderPage = storeOrderRepository.findByStatusIn(statuses, pageable);
        if (storeOrderPage.isEmpty()) {
            throw new IllegalArgumentException("주문을 찾을 수 없습니다.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        //페이지의 주문 상세를 DTO로 매핑해서 반환
        List<StoreOrderCheckResponseDto> orderDetails = storeOrderPage.getContent().stream()
            .flatMap(storeOrder -> {
                List<StoreOrderDetail> storeOrderDetails = storeOrderDetailRepository.findByStoreOrderId(
                    storeOrder.getId());

			return storeOrderDetails.stream()
			       .map(detail -> new StoreOrderCheckResponseDto(
							storeOrder.getId(),
							storeOrder.getCreatedDateTime().format(formatter), 
                            storeOrder.getPrice(),
                            storeOrder.getStatus(),
                            storeOrder.getStore().getAddress().getAddressBase(),
                            storeOrder.getStore().getAddress().getAddressDetail(),
                            storeOrder.getStore().getAddress().getZipcode(),
                            storeOrder.getStore().getName(),
                            storeOrder.getStore().getPhone(),
                            storeOrder.getStore().getId(),
                            detail.getStoreMenu().getName(),
                            storeOrder.getPrice(),
                            detail.getStoreMenu().getArticleUnit(),
                            detail.getStoreMenu().getPictureUrl(),
                            storeOrder.getQuantity(),
                            detail.getStoreMenu().getPrice()

                    ));
            })

				   .collect(Collectors.toList());
				   return new PageImpl<>(orderDetails, pageable, storeOrderPage.getTotalElements());

	}
    //배송중
    public Page<StoreOrderCheckResponseDto> getStoreOrdercheckd(int pageNumber, int pageSize) {
        System.out.println("StoreOrderService.getStoreOrdercheckd");
        //Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
         // ACCEPTED 상태인 주문을 조회할 상태 리스트 생성
        List<StoreOrderStatus> statuses = Arrays.asList(StoreOrderStatus.DELIVERING);
        // 상태가 ACCEPTED인 모든 주문을 페이지로 조회
        Page<StoreOrder> storeOrderPage = storeOrderRepository.findByStatusIn(statuses, pageable);
        if (storeOrderPage.isEmpty()) {
            throw new IllegalArgumentException("주문을 찾을 수 없습니다.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        //페이지의 주문 상세를 DTO로 매핑해서 반환
        List<StoreOrderCheckResponseDto> orderDetails = storeOrderPage.getContent().stream()
            .flatMap(storeOrder -> {
                List<StoreOrderDetail> storeOrderDetails = storeOrderDetailRepository.findByStoreOrderId(
                    storeOrder.getId());

			return storeOrderDetails.stream()
			       .map(detail -> new StoreOrderCheckResponseDto(
							storeOrder.getId(),
							storeOrder.getCreatedDateTime().format(formatter), 
                            storeOrder.getPrice(),
                            storeOrder.getStatus(),
                            storeOrder.getStore().getAddress().getAddressBase(),
                            storeOrder.getStore().getAddress().getAddressDetail(),
                            storeOrder.getStore().getAddress().getZipcode(),
                            storeOrder.getStore().getName(),
                            storeOrder.getStore().getPhone(),
                            storeOrder.getStore().getId(),
                            detail.getStoreMenu().getName(),
                            storeOrder.getPrice(),
                            detail.getStoreMenu().getArticleUnit(),
                            detail.getStoreMenu().getPictureUrl(),
                            storeOrder.getQuantity(),
                            detail.getStoreMenu().getPrice()

                    ));
            })

				   .collect(Collectors.toList());
				   return new PageImpl<>(orderDetails, pageable, storeOrderPage.getTotalElements());

	}
    //배송완료
    public Page<StoreOrderCheckResponseDto> getStoreOrdercheckdc(int pageNumber, int pageSize) {
        System.out.println("StoreOrderService.getStoreOrdercheckdc");
        //Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
         // ACCEPTED 상태인 주문을 조회할 상태 리스트 생성
        List<StoreOrderStatus> statuses = Arrays.asList(StoreOrderStatus.COMPLETED);
        // 상태가 ACCEPTED인 모든 주문을 페이지로 조회
        Page<StoreOrder> storeOrderPage = storeOrderRepository.findByStatusIn(statuses, pageable);
        if (storeOrderPage.isEmpty()) {
            throw new IllegalArgumentException("주문을 찾을 수 없습니다.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        //페이지의 주문 상세를 DTO로 매핑해서 반환
        List<StoreOrderCheckResponseDto> orderDetails = storeOrderPage.getContent().stream()
            .flatMap(storeOrder -> {
                List<StoreOrderDetail> storeOrderDetails = storeOrderDetailRepository.findByStoreOrderId(
                    storeOrder.getId());

			return storeOrderDetails.stream()
			       .map(detail -> new StoreOrderCheckResponseDto(
							storeOrder.getId(),
							storeOrder.getCreatedDateTime().format(formatter), 
                            storeOrder.getPrice(),
                            storeOrder.getStatus(),
                            storeOrder.getStore().getAddress().getAddressBase(),
                            storeOrder.getStore().getAddress().getAddressDetail(),
                            storeOrder.getStore().getAddress().getZipcode(),
                            storeOrder.getStore().getName(),
                            storeOrder.getStore().getPhone(),
                            storeOrder.getStore().getId(),
                            detail.getStoreMenu().getName(),
                            storeOrder.getPrice(),
                            detail.getStoreMenu().getArticleUnit(),
                            detail.getStoreMenu().getPictureUrl(),
                            storeOrder.getQuantity(),
                            detail.getStoreMenu().getPrice()

                    ));
            })

				   .collect(Collectors.toList());
				   return new PageImpl<>(orderDetails, pageable, storeOrderPage.getTotalElements());

	}
    



    //비품, 식자재 주문 확인(직영점)
    public Page<StoreOrderCheckResponseDto> getStoreOrdercheckforstore(String adminLoginId, int pageNumber,
        int pageSize) {
        System.out.println("StoreOrderService.getStoreOrdercheckforstore");

        Store store = storeRepository.findByManagerLoginId(adminLoginId)
            .orElseThrow(() -> new IllegalArgumentException("해당 상점을 찾을 수 없습니다."));

        Long storeId = store.getId();

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // 상점의 주문을 페이지로 조회
        List<StoreOrderStatus> statuses = Arrays.asList(
            StoreOrderStatus.WAITING,
            StoreOrderStatus.ACCEPTED,
            StoreOrderStatus.DELIVERING,
            StoreOrderStatus.COMPLETED
        );
        Page<StoreOrder> storeOrderPage = storeOrderRepository.findByStoreIdAndStatusIn(storeId, statuses, pageable);

        if (storeOrderPage.isEmpty()) {
            throw new IllegalArgumentException("해당 상점의 주문을 찾을 수 없습니다.");
        }

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");


        // 페이지의 주문 상세를 DTO로 매핑하여 반환
        List<StoreOrderCheckResponseDto> orderDetails = storeOrderPage.getContent().stream()
            .flatMap(storeOrder -> {
                List<StoreOrderDetail> storeOrderDetails = storeOrderDetailRepository.findByStoreOrderId(
                    storeOrder.getId());

                return storeOrderDetails.stream()
                    .map(detail -> new StoreOrderCheckResponseDto(
						
                            storeOrder.getId(),
                            storeOrder.getCreatedDateTime().format(formatter), 
                            storeOrder.getPrice(),
                            storeOrder.getStatus(),
                            store.getAddress().getAddressBase(),
                            store.getAddress().getAddressDetail(),
                            store.getAddress().getZipcode(),
                            store.getName(),
                            store.getPhone(),
                            storeId,
                            detail.getStoreMenu().getName(),
                            storeOrder.getPrice(),
                            detail.getStoreMenu().getArticleUnit(),
                            detail.getStoreMenu().getPictureUrl(),
                            storeOrder.getQuantity(),
                            detail.getStoreMenu().getPrice()
                    ));

            })
            .collect(Collectors.toList());

        return new PageImpl<>(orderDetails, pageable, storeOrderPage.getTotalElements());
    }

    //비품, 식자재 주문 상태 변경

    // 1. WAITING -> ACCEPTED
    public void acceptedStoreOrder(Long storeOrderId) {
        StoreOrder order = storeOrderRepository.findById(storeOrderId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        order.acceptedOrder();
        storeOrderRepository.save(order);

    }

    // 2.ACCEPTED -> DELIVERING
    public void deliveringStoreOrder(Long storeOrderId) {
        StoreOrder order = storeOrderRepository.findById(storeOrderId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        order.deliveringOrder();
        storeOrderRepository.save(order);

    }

    // 3. DELIVERING -> COMPLETED

    public void completeStoreOrder(Long storeOrderId) {
        StoreOrder order = storeOrderRepository.findById(storeOrderId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));
        order.completeOrder();
        storeStockService.increaseStockOnOrder(order);

    }

    private String getManagerLoginId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ManagerUserDetails managerUserDetails = (ManagerUserDetails)principal;

        return managerUserDetails.getUsername();
    }

}




















