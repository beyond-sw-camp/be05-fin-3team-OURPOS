package com.ourpos.api.storeorder.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.storeorder.dto.request.StoreCommRequestDto;
import com.ourpos.api.storeorder.dto.response.StoreCommResponseDto;
import com.ourpos.api.storeorder.service.StoreCommService;
import com.ourpos.api.storeorder.service.StoreCommServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class StoreCommController implements StoreCommControllerDocs {

    private final StoreCommService storeCommService;
    private final StoreCommServiceImpl storeCommServiceImpl;

    // 새로운 비품,식자재를 추가하는 코드
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/storecomms")
    public Result<Void> addStoreComm(@RequestBody StoreCommRequestDto storeCommRequestDto) {
        log.info("비품/식자재추가 : {}", storeCommRequestDto);
        storeCommService.addStoreComm(storeCommRequestDto);
        return new Result<>(HttpStatus.OK.value(), "비품/식자재 추가가 완료되었습니다.", null);
    }
    /*
    //  비품,식자재 조회
    @GetMapping("/getstorecomms")
    public Result<List<StoreCommResponseDto>> getStoreComms() {
        log.info("비품/식자재 목록 출력");
        List<StoreCommResponseDto> storeCommsList = storeCommService.getStoreComms();
        return new Result<>(HttpStatus.OK.value(), "전체 비품/식자재 목록을 불러옵니다.", storeCommsList);
    }
     */

    // 비품,식자재 수정
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/storecomms/{storeCommId}")
    public Result<Void> updateStoreComm(@Valid @PathVariable Long storeCommId,
        @RequestBody StoreCommRequestDto storeCommRequestDto) {
        log.info("제품 인덱스로 비품/식자재 정보 수정");
        storeCommService.updateStoreComm(storeCommId, storeCommRequestDto);
        return new Result<>(HttpStatus.OK.value(), "비품/식자재 정보 수정이 완료되었습니다.", null);
    }

    // 비품 식자재 삭제
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/storecomms/{storeCommId}/delete")
    public Result<Void> restoreStoreComm(@PathVariable Long storeCommId) {
        log.info("비품/식자재 삭제 : {}", storeCommId);
        storeCommService.deletetStoreComm(storeCommId);
        return new Result<>(HttpStatus.OK.value(), "비품/식자재가 삭제되었습니다.", null);
    }

}
