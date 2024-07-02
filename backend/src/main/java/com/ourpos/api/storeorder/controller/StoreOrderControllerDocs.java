package com.ourpos.api.storeorder.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ourpos.api.Result;
import com.ourpos.api.storeorder.dto.request.StoreCommRequestDto;
import com.ourpos.api.storeorder.dto.request.StoreOrderRequestDto;
import com.ourpos.api.storeorder.dto.response.StoreCommResponseDto;
import com.ourpos.api.storeorder.dto.response.StoreOrderCheckResponseDto;
import com.ourpos.api.storeorder.dto.response.StoreOrderResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "비품/식자재 주문 ", description = "비품/식자재 주문 관련 API")
@RequestMapping("/api/v1/storeorder")
public interface StoreOrderControllerDocs {

	@Operation(summary = "비품, 식자재 목록 확인", description = "직영점의 사장은 주문 가능한 비품 및 식자재의 목록을 확인 할 수 있습니다.")
	@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = StoreCommResponseDto.class)))
	@GetMapping("/checkstorecomms/ingredients")
	public Result<Page<StoreCommResponseDto>> getIngredients(Pageable pageable);
	
	@Operation(summary = "비품, 식자재 목록 확인", description = "직영점의 사장은 주문 가능한 비품 및 식자재의 목록을 확인 할 수 있습니다.")
	@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = StoreCommResponseDto.class)))
	@GetMapping("/checkstorecomms/supplies")
	public Result<Page<StoreCommResponseDto>> getSupplies(Pageable pageable);
	

	

	@Operation(summary = "비품, 식자재 주문", description = "직영점의 사장은 필요한 비품이나 식자재를 본사에 요청(주문) 할 수 있다.")
	@Parameters(value = {
		@Parameter(name = "storeCommId", description = "주문할 가게물품의 아이디"),
		@Parameter(name = "storeId", description = "주문할 가게의 아이디"),
		@Parameter(name = "storeOrderDetailQuantity", description = "필요한 가게물품의 수량"),
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "식자재, 비품 주문이 완료되었습니다.", content = @Content(schema = @Schema(implementation = StoreCommRequestDto.class)))
	})
	@PostMapping("/storecomms/order")
	public Result<Void> createStoreOrder(@Valid @RequestBody StoreOrderRequestDto storeOrderRequestDto);

	@Operation(summary = "비품, 식자재 주문 내역 확인", description = "직영점의 사장은 주문 상세 ID(`orderdetailId`)로 주문 내역의 조회가 가능합니다.")
	@GetMapping("/storeorder/{orderDetailId}")
	@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = StoreOrderResponseDto.class)))
	public Result<StoreOrderResponseDto> getStoreOrder(@PathVariable Long orderDetailId);

	@Operation(summary = "비품, 식자재 주문 삭제", description = "직영점의 사장은 주문 상세 ID(orderdetailId)로 주문 내역의 삭제가 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "식자재, 비품 주문이 삭제되었습니다.", content = @Content(schema = @Schema(implementation = StoreCommRequestDto.class)))
	})
	@PostMapping("/storecomms/order")
	public Result<Void> deleteStoreOrder(@PathVariable Long orderdetailId);

	@Operation(summary = "비품, 식자재 주문 확인 (본사)", description = "본사의 관리자는 직영점에서 주문한 비품 및 식자재에 대한 주문 내역을 확인 할 수 있습니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "식자재, 비품 주문 목록을 불러옵니다.", content = @Content(schema = @Schema(implementation = StoreOrderCheckResponseDto.class)))
	})
	public ResponseEntity<Result<Page<StoreOrderCheckResponseDto>>> getStoreOrderCheckw( Pageable pageable);

	@Operation(summary = "비품, 식자재 주문 확인 (본사)", description = "본사의 관리자는 직영점에서 주문한 비품 및 식자재에 대한 주문 내역을 확인 할 수 있습니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "식자재, 비품 주문 목록을 불러옵니다.", content = @Content(schema = @Schema(implementation = StoreOrderCheckResponseDto.class)))
	})
	public ResponseEntity<Result<Page<StoreOrderCheckResponseDto>>> getStoreOrderChecka( Pageable pageable);
	@Operation(summary = "비품, 식자재 주문 확인 (본사)", description = "본사의 관리자는 직영점에서 주문한 비품 및 식자재에 대한 주문 내역을 확인 할 수 있습니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "식자재, 비품 주문 목록을 불러옵니다.", content = @Content(schema = @Schema(implementation = StoreOrderCheckResponseDto.class)))
	})
	public ResponseEntity<Result<Page<StoreOrderCheckResponseDto>>> getStoreOrderCheckd( Pageable pageable);
	@Operation(summary = "비품, 식자재 주문 확인 (본사)", description = "본사의 관리자는 직영점에서 주문한 비품 및 식자재에 대한 주문 내역을 확인 할 수 있습니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "식자재, 비품 주문 목록을 불러옵니다.", content = @Content(schema = @Schema(implementation = StoreOrderCheckResponseDto.class)))
	})
	public ResponseEntity<Result<Page<StoreOrderCheckResponseDto>>> getStoreOrderCheckdk( Pageable pageable);

	@Operation(summary="비품, 식자재 주문 상태 변경( WAITING -> ACCEPTED)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "주문이 승인되었습니다.")
	})
	public Result<Void> acceptedStoreOrder(@PathVariable Long storeOrderId);

	@Operation(summary="비품, 식자재 주문 상태 변경( ACCEPTED -> DELIVERING)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "배달이 승인되었습니다.")
	})
	public Result<Void> deliveringStoreOrder(@PathVariable Long storeOrderId);

	@Operation(summary="비품, 식자재 주문 상태 변경( DELIVERING -> COMPLETE)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "배달이 완료되었습니다.")
	})
	public Result<Void> completeStoreOrder(@PathVariable Long storeOrderId);


}
