package com.ourpos.api.store.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.domain.store.StoreStock;
import com.ourpos.domain.store.StoreStockRepository;
import com.ourpos.domain.storeorder.StoreOrder;
import com.ourpos.domain.storeorder.StoreOrderDetail;

@Service
public class StoreStockServiceImpl implements StoreStockService {
	private final StoreStockRepository storeStockRepository;

	@Autowired
	public StoreStockServiceImpl(StoreStockRepository storeStockRepository) {
		this.storeStockRepository = storeStockRepository;
	}

	// 본사에 주문한 재고가 배송 완료되면 storestock 상승
	@Transactional
	public void increaseStockOnOrder(StoreOrder storeOrder) {
		List<StoreOrderDetail> storeOrderDetails = storeOrder.getStoreOrderDetails();
		for (StoreOrderDetail storeOrderDetail : storeOrderDetails) {
			if (storeOrderDetail.getStoreOrder().getStore() == null) {
				throw new IllegalArgumentException("해당 주문의 매장 정보를 찾을 수 없습니다.");
			}

			List<StoreStock> storeStocks = storeStockRepository.findByStoreIdAndStoreCommId(
				storeOrderDetail.getStoreOrder().getStore().getId(),
				storeOrderDetail.getStoreMenu().getId()
			);

			if (storeStocks.isEmpty()) {
				//재고 없으면 새로 넣음
				StoreStock newStoreStock = StoreStock.builder()
					.store(storeOrderDetail.getStoreOrder().getStore())
					.storeComm(storeOrderDetail.getStoreMenu())
					.quantity(storeOrderDetail.getStoreOrder().getQuantity())
					.build();
				storeStockRepository.save(newStoreStock);

			} else {
				for (StoreStock storeStock : storeStocks) {
					storeStock.increaseQuantity(storeOrderDetail.getStoreOrder().getQuantity());
				}
				storeStockRepository.saveAll(storeStocks);
			}
		}
	}

	// 아침에 들어온 재고는 고정으로 조회 (이후 재고 상태 반영 x 오로지 입고된 재고의 현황만 파악)
	// 재고의 상태가 주문 승인, 배송 중인 재고는 입고 예정량으로 조회
	// 재고의 입출고가 반영되어서 현재 남아있는 실시간 재고량 조회
	// 점주가 기타입출고 조정 위해 상태 확인 후 임의 삭제 가능


}

