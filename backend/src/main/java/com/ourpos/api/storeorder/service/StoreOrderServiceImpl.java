package com.ourpos.api.storeorder.service;

import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.store.service.StoreStockServiceImpl;
import com.ourpos.api.storeorder.dto.request.StoreOrderRequestDto;
import com.ourpos.api.storeorder.dto.response.StoreCommResponseDto;
import com.ourpos.api.storeorder.dto.response.StoreOrderCheckResponseDto;
import com.ourpos.api.storeorder.dto.response.StoreOrderResponseDto;
import com.ourpos.domain.recipe.RecipeRepository;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;
import com.ourpos.domain.store.StoreStock;
import com.ourpos.domain.store.StoreStockRepository;
import com.ourpos.domain.storeorder.StoreComm;
import com.ourpos.domain.storeorder.StoreCommRepository;
import com.ourpos.domain.storeorder.StoreOrder;
import com.ourpos.domain.storeorder.StoreOrderDetail;
import com.ourpos.domain.storeorder.StoreOrderDetailRepository;
import com.ourpos.domain.storeorder.StoreOrderRepository;

import lombok.RequiredArgsConstructor;

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

	// 판매 비품, 식자재 목록 확인
	public List<StoreCommResponseDto> checkStoreComms() {
		System.out.println("StoreCommServiceImplServiceImpl.getStoreComms");
		List<StoreComm> storeComms = storeCommRepository.findAll();
		List<StoreCommResponseDto> storeCommResponseDtos = new ArrayList<>();

		for (StoreComm storeComm : storeComms) {
			storeCommResponseDtos.add(new StoreCommResponseDto(storeComm));
		}
		return storeCommResponseDtos;
	}

	// 비품, 식자재 주문 (비품,식자재 주문 관리에서 배달완료 시 재고에 반영)
	public void createStoreOrder(StoreOrderRequestDto requestDto) {

		// StoreOrder 생성
		Store store = storeRepository.findById(requestDto.getStoreId())
			.orElseThrow(() -> new IllegalArgumentException("해당 store를 찾을 수 없습니다."));

		StoreOrder storeOrder = StoreOrder.builder()
			.price(0)
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
	public StoreOrderCheckResponseDto getStoreOrdercheck(Long storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

		return new StoreOrderCheckResponseDto();

	}

	 */

	public List<StoreOrderCheckResponseDto> getStoreOrdercheck(Long storeId) {
		System.out.println("StoreOrderService.getStoreOrdercheck");

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new IllegalArgumentException("해당 상점을 찾을 수 없습니다."));

		List<StoreOrder> storeOrders = storeOrderRepository.findByStoreId(storeId);
		if (storeOrders.isEmpty()) {
			throw new IllegalArgumentException("해당 상점의 주문을 찾을 수 없습니다.");
		}

		List<StoreOrderCheckResponseDto> storeOrderCheckResponseDtos = new ArrayList<>();

		for (StoreOrder storeOrder : storeOrders) {
			List<StoreOrderDetail> storeOrderDetails = storeOrderDetailRepository.findByStoreOrderId(
				storeOrder.getId());
			for (StoreOrderDetail storeOrderDetail : storeOrderDetails) {
				StoreOrderCheckResponseDto dto = new StoreOrderCheckResponseDto(
					store.getId(),
					storeOrderDetail.getStoreMenu().getName(),
					storeOrder.getPrice(),
					storeOrderDetail.getStoreMenu().getArticleUnit(),
					storeOrderDetail.getStoreMenu().getPictureUrl(),
					storeOrder.getQuantity(),
					storeOrderDetail.getStoreMenu().getPrice()
				);
				storeOrderCheckResponseDtos.add(dto);
			}
		}

		return storeOrderCheckResponseDtos;
	}

		//비품, 식자재 주문 상태 변경

		// 1. WAITING -> ACCEPTED
		public void acceptedStoreOrder (Long storeOrderId){
			StoreOrder order = storeOrderRepository.findById(storeOrderId)
				.orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

			order.acceptedOrder();
			storeOrderRepository.save(order);

		}

		// 2.ACCEPTED -> DELIVERING
		public void deliveringStoreOrder (Long storeOrderId){
			StoreOrder order = storeOrderRepository.findById(storeOrderId)
				.orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

			order.deliveringOrder();
			storeOrderRepository.save(order);

		}

		// 3. DELIVERING -> COMPLETED

		public void completeStoreOrder (Long storeOrderId){
			StoreOrder order = storeOrderRepository.findById(storeOrderId)
				.orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));
			order.completeOrder();
			storeStockService.increaseStockOnOrder(order);

		}

	}




















