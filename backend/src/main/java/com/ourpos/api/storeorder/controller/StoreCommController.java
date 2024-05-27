package com.ourpos.api.storeorder.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.storeorder.dto.request.StoreCommRequestDto;
import com.ourpos.api.storeorder.dto.request.StoreCommResponseDto;
import com.ourpos.api.storeorder.service.StoreCommService;
import com.ourpos.api.storeorder.service.StoreCommServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/storeorder")
public class StoreCommController {

    private final StoreCommService storeCommService;
    private final StoreCommServiceImpl storeCommServiceImpl;

    // 새로운 비품,식자재를 추가하는 코드
    @PostMapping("/addstorecomm")
    public Result<Void> addStoreComm(@RequestBody StoreCommRequestDto storeCommRequestDto) {
        log.info("비품/식자재추가 : {}", storeCommRequestDto);
        storeCommService.addStoreComm(storeCommRequestDto);
        return new Result<>(HttpStatus.OK.value(), "비품/식자재 주문이 완료되었습니다.", null);
    }

    //  비품,식자재 조회
    @GetMapping("/getstorecomms")
    public Result<List<StoreCommResponseDto>> getStoreComms() {
        log.info("비품/식자재 목록 출력");
        List<StoreCommResponseDto> storeCommsList = storeCommService.getStoreComms();
        return new Result<>(HttpStatus.OK.value(), "전체 비품/식자재 목록을 불러옵니다.", storeCommsList);
    }

    // 비품,식자재 수정
    @PutMapping("/modifystorecomm/{storeCommId}")
    public Result<Void> updateStoreComm(@PathVariable Long StoreCommId,
        @RequestBody StoreCommRequestDto storeCommRequestDto) {
        log.info("제품 인덱스로 비품/식자재 정보 수정");
        storeCommService.updateStoreComm(StoreCommId, storeCommRequestDto);
        return new Result<>(HttpStatus.OK.value(), "비품/식자재 정보 수정이 완료되었습니다.", null);
    }

    // 비품 식자재 삭제
    @PutMapping("/deletestorecomm/{storeCommId}")
    public Result<Void> restoreStoreComm(@PathVariable Long storeCommId) {
        log.info("비품/식자재 삭제 : {}", storeCommId);
        storeCommService.deletetStoreComm(storeCommId);
        return new Result<>(HttpStatus.OK.value(), "비품/식자재가 삭제되었습니다.", null);
    }

}
