package com.ourpos.api.storeorder.service;

import java.util.List;

import com.ourpos.api.storeorder.dto.request.StoreCommRequestDto;
import com.ourpos.api.storeorder.dto.request.StoreCommResponseDto;

public interface StoreCommService {
    void addStoreComm(StoreCommRequestDto storeCommRequestDto);
    List<StoreCommResponseDto> getStoreComms();

    void updateStoreComm(Long StoreCommId, StoreCommRequestDto storeCommRequestDto);
}