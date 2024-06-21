package com.ourpos.api.storeorder.controller;

import static com.ourpos.domain.store.QStore.store;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.storeorder.dto.request.StoreOrderRequestDto;
import com.ourpos.api.storeorder.dto.response.StoreCommResponseDto;
import com.ourpos.api.storeorder.dto.response.StoreOrderCheckResponseDto;
import com.ourpos.api.storeorder.dto.response.StoreOrderResponseDto;
import com.ourpos.api.storeorder.service.StoreCommServiceImpl;
import com.ourpos.api.storeorder.service.StoreOrderServiceImpl;
import com.ourpos.auth.dto.manager.ManagerUserDetails;
import com.ourpos.domain.storeorder.StoreOrderDetail;
import com.ourpos.domain.storeorder.StoreOrderDetailRepository;
import com.ourpos.domain.storeorder.StoreOrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class StoreOrderController implements StoreOrderControllerDocs {

    private final StoreCommServiceImpl storeCommService;
    private final StoreOrderDetailRepository storeOrderDetailRepository;
    private final StoreOrderRepository storeOrderRepository;
    private final StoreOrderServiceImpl storeOrderService;

    // 판매 비품, 식자재 목록 확인(페이징 처리 x)
    /* 
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/storecomms")
    public Result<List<StoreCommResponseDto>> checkStoreComms() {
        log.info("비품/식자재 목록 출력");
        List<StoreCommResponseDto> storeCommsList = storeCommService.getStoreComms();
        return new Result<>(HttpStatus.OK.value(), " 목록을 불러옵니다.", storeCommsList);
    }
    */
     // 판매 비품, 식자재 목록 확인(페이징 처리 o)
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/storecomms")
    public Result<Page<StoreCommResponseDto>> checkStoreComms(Pageable pageable) {
        log.info("비품/식자재 목록 출력");
        Page<StoreCommResponseDto> storeCommsPage = storeOrderService.checkStoreComms(pageable);
        return new Result<>(HttpStatus.OK.value(), " 목록을 불러옵니다.", storeCommsPage);
    }

    // 비품, 식자재 주문 (비품,식자재 주문 관리에서 배달완료 시 재고에 반영) ?-> price가 반영 안됨
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/storecomms/order")
    public Result<Void> createStoreOrder(@Valid @RequestBody StoreOrderRequestDto storeOrderRequestDto) {
        log.info("가게 식자재, 비품 주문: {}", storeOrderRequestDto);
        storeOrderService.createStoreOrder(storeOrderRequestDto);
        return new Result<>(HttpStatus.CREATED.value(), "식자재, 비품 주문이 완료되었습니다.", null);
    }

    // 비품, 식자재 주문 내역 확인 ? 주문 내역  개별 조회 o
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/storeorder/{orderDetailId}")
    public Result<StoreOrderResponseDto> getStoreOrder(@PathVariable Long orderDetailId) {
        log.info("가게 식자재, 비품 주문: {}", orderDetailId);
        StoreOrderResponseDto storeOrderResponseDto = storeOrderService.getStoreOrder(orderDetailId);
        return new Result<>(HttpStatus.OK.value(), "식자재, 비품 주문이 완료되었습니다.", storeOrderResponseDto);
    }

    //비품, 식자재 주문 확인 (직영점)
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/storeorder/checkforstore/my")
    public ResponseEntity<Result<Page<StoreOrderCheckResponseDto>>> getStoreOrdercheckforstore(Pageable pageable) {
        String adminLoginId = getManagerLoginId();

        log.info("가게 식자재, 비품 주문: {}", adminLoginId);
        log.debug("getStoreOrdercheckforstore 메서드 호출됨");

        Page<StoreOrderCheckResponseDto> storeOrderPage = storeOrderService.getStoreOrdercheckforstore(adminLoginId, pageable.getPageNumber(), pageable.getPageSize());
        Result<Page<StoreOrderCheckResponseDto>> result = new Result<>(HttpStatus.OK.value(), "식자재, 비품 주문 목록을 불러옵니다.", storeOrderPage);
        return ResponseEntity.ok(result);
    }

   


    /*
    // 비품, 식자재 주문 수정
    @PutMapping("/storeorder/{orderdetailId}")
    public Result<Void> updateStoreOrder(@Valid @PathVariable Long orderdetailId,
        @RequestBody StoreOrderRequestDto requestDto) {
        log.info("가게 식자재, 비품 주문 수정: {}", orderdetailId);
        storeOrderService.updateStoreOrder(orderdetailId, requestDto);
        return new Result<>(HttpStatus.OK.value(), "주문 수정 성공", null);
    }

     */

    // 비품, 식자재 주문 삭제
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/storeorder/{orderdetailId}")
    public Result<Void> deleteStoreOrder(@PathVariable Long orderdetailId) {

        log.info("비품/식자재 삭제 : {}", orderdetailId);
        StoreOrderDetail storeOrderDetail = storeOrderDetailRepository.findById(orderdetailId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));
        // storeorderdetail 삭제
        storeOrderDetailRepository.delete(storeOrderDetail);
        // storeorder 삭제
        if (storeOrderDetail.getStoreOrder() != null) {
            storeOrderRepository.delete(storeOrderDetail.getStoreOrder());
        }
        return new Result<>(HttpStatus.OK.value(), "주문이 삭제되었습니다.", null);
    }

    //비품, 식자재 주문 확인 (본사)
    //@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/storeorder/{storeId}/check")
    public ResponseEntity<Result<Page<StoreOrderCheckResponseDto>>> getStoreOrderCheck(@PathVariable Long storeId, Pageable pageable) {
        log.info("가게 식자재, 비품 주문: {}", storeId);
        log.debug("getStoreOrderCheck 메서드 호출됨");
        
            Page<StoreOrderCheckResponseDto> storeOrderPage = storeOrderService.getStoreOrdercheck(storeId, pageable.getPageNumber(),pageable.getPageSize());
    
            // 로그 추가: 데이터 로드 성공 로그
            log.debug("주문 목록 데이터 로드 성공: {}", storeOrderPage);
    
            Result<Page<StoreOrderCheckResponseDto>> result = new Result<>(HttpStatus.OK.value(), "식자재, 비품 주문 목록을 불러옵니다.", storeOrderPage);
            return ResponseEntity.ok(result);
        
    }

    //비품, 식자재 주문 상태 변경

    // 1. WAITING -> ACCEPTED
    //@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/storeorder/{storeOrderId}/accepted")
    public Result<Void> acceptedStoreOrder(@PathVariable Long storeOrderId) {
        log.info("가게 식자재, 비품 주문 수정: {}", storeOrderId);
        storeOrderService.acceptedStoreOrder(storeOrderId);
        return new Result<>(HttpStatus.OK.value(), "주문이 승인되었습니다.", null);
    }

    // 2. ACCEPTED -> DELIVERING
    //@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/storeorder/{storeOrderId}/delivering")
    public Result<Void> deliveringStoreOrder(@PathVariable Long storeOrderId) {
        log.info("가게 식자재, 비품 주문 수정: {}", storeOrderId);
        storeOrderService.deliveringStoreOrder(storeOrderId);
        return new Result<>(HttpStatus.OK.value(), "배달이 시작되었습니다.", null);
    }

    // 3. DELIVERING -> COMPLETED
    //@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/storeorder/{storeOrderId}/complete")
    public Result<Void> completeStoreOrder(@PathVariable Long storeOrderId) {
        log.info("가게 식자재, 비품 주문 수정: {}", storeOrderId);
        storeOrderService.completeStoreOrder(storeOrderId);
        return new Result<>(HttpStatus.OK.value(), "배달이 완료되었습니다.", null);
    }

    private String getManagerLoginId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ManagerUserDetails managerUserDetails = (ManagerUserDetails)principal;

        return managerUserDetails.getUsername();
    }

}
