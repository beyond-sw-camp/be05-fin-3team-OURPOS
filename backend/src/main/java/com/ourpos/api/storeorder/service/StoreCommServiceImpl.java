package com.ourpos.api.storeorder.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.storeorder.dto.request.StoreCommRequestDto;
import com.ourpos.api.storeorder.dto.request.StoreCommResponseDto;
import com.ourpos.domain.storeorder.StoreComm;
import com.ourpos.domain.storeorder.StoreCommRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreCommServiceImpl implements StoreCommService {

    private final StoreCommRepository storeCommRepository;

    // 매장 비품/식자재에 새로운 제품 추가
    @Override
    public void addStoreComm(StoreCommRequestDto storeCommRequestDto) {
        saveStoreComm(storeCommRequestDto);
    }

    @Override
    public List<StoreCommResponseDto> getStoreComms() {
        System.out.println("StoreCommServiceImplServiceImpl.getStoreComms");
        List<StoreComm> storeComms = storeCommRepository.findAll();
        List<StoreCommResponseDto> storeCommResponseDtos = new ArrayList<>();

        for (StoreComm storeComm : storeComms) {
            storeCommResponseDtos.add(new StoreCommResponseDto(storeComm));
        }
        return storeCommResponseDtos;
    }

    @Override
    public void updateStoreComm(Long storeCommId, StoreCommRequestDto storeCommRequestDto) {
        StoreComm existingStoreComm = storeCommRepository.findById(storeCommId)
            .orElseThrow(() -> new RuntimeException("StoreComm not found with id: " + storeCommId));
        existingStoreComm.updateFromDto(storeCommRequestDto);
        storeCommRepository.save(existingStoreComm);
    }

    private void saveStoreComm(StoreCommRequestDto storeCommRequestDto) {
        StoreComm storeComm = storeCommRequestDto.toEntity();
        storeCommRepository.save(storeComm);
    }

    @Override
    @Transactional
    public void deletetStoreComm(Long storeCommId) {
        StoreComm storeComm = storeCommRepository.findById(storeCommId)
            .orElseThrow(() -> new RuntimeException("StoreComm not found with id: " + storeCommId));
        storeComm.delete();
        storeCommRepository.save(storeComm);
    }

}
