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

	@Transactional
	public void increaseStockOnOrder(StoreOrder storeOrder){
		List<StoreOrderDetail> storeOrderDetails = storeOrder.getStoreOrderDetails();
		for(StoreOrderDetail storeOrderDetail : storeOrderDetails){
			if(storeOrderDetail.getStoreOrder().getStore()==null){
				throw new IllegalArgumentException("해당 주문의 매장 정보를 찾을 수 없습니다.");
			}

			List<StoreStock> storeStocks = storeStockRepository.findByStoreIdAndStoreCommId(
				storeOrderDetail.getStoreOrder().getStore().getId(),
				storeOrderDetail.getStoreMenu().getId()
			);

			if(storeStocks.isEmpty()){
				//재고 없으묜 새로 생성
				StoreStock newStoreStock = StoreStock.builder()
					.store(storeOrderDetail.getStoreOrder().getStore())
					.storeComm(storeOrderDetail.getStoreMenu())
					.quantity(storeOrderDetail.getStoreOrder().getQuantity())
					.build();
				storeStockRepository.save(newStoreStock);

			}else {
				for (StoreStock storeStock :storeStocks){
					storeStock.increaseQuantity(storeOrderDetail.getStoreOrder().getQuantity());
				}
				storeStockRepository.saveAll(storeStocks);
			}
		}
	}
}
