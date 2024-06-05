package com.ourpos.api.storeorder.controller;

import java.util.List;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ourpos.api.Result;
import com.ourpos.api.storeorder.dto.request.StoreCommRequestDto;
import com.ourpos.api.storeorder.dto.response.StoreCommResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "비품/식자재 주문 관리 " , description = "비품/식자재 주문 관리 관련 API")
@RequestMapping("/api/v1/storeorder")
public interface StoreCommControllerDocs {

	@Operation(summary = "새로운 비품, 식자재 추가", description = "본사 관리자는 직영점에서 주문할 수 있는 비품 및 식자재를 추가할 수 있다.")
	@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = StoreCommResponseDto.class)))
	@PostMapping("/storecomms")
	public Result<Void> addStoreComm(@RequestBody StoreCommRequestDto storeCommRequestDto);

	@Operation(summary = "새로운 비품, 식자재 수정", description = "본사 관리자는 직영점에서 주문할 수 있는 비품 및 식자재에 대한 상세 정보를  가게 상품 ID{storeCommId} 로 수정 할 수 있다.")
	@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = StoreCommResponseDto.class)))
	@PutMapping("/storecomms/{storeCommId}")
	public Result<Void> updateStoreComm(@Valid @PathVariable Long storeCommId,
		@RequestBody StoreCommRequestDto storeCommRequestDto);

	@Operation(summary = "비품, 식자재 삭제", description = "본사 관리자는 직영점에서 주문할 수 있는 비품 및 식자재를 가게 상품ID`{storeCommId}` 로 삭제 할 수 있다.")
	@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = StoreCommResponseDto.class)))
	@PutMapping("/storecomms/{storeCommId}/delete")
	public Result<Void> restoreStoreComm(@PathVariable Long storeCommId);


}
