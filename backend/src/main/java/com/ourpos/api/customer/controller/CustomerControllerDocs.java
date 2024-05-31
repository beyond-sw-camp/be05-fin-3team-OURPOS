package com.ourpos.api.customer.controller;



import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ourpos.api.Result;
import com.ourpos.api.customer.dto.request.CustomerAddressRequestDto;
import com.ourpos.api.customer.dto.request.CustomerAddressUpdateDto;
import com.ourpos.api.customer.dto.response.CustomerAddressResponseDto;
import com.ourpos.api.customer.dto.response.CustomerResponseDto;
import com.ourpos.api.order.dto.response.DeliveryOrderResponseDto;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "마이페이지 API")
@RequestMapping("api/v1/customers")
public interface CustomerControllerDocs {

	@Operation(summary = "마이페이지-개인정보 조회" , description = "사용자는 가입 시 입력한 개인 정보를 조회 할 수 있습니다.")
	@Parameters(value = {
		@Parameter(name = "customerId", description = "고객 Id"),
		@Parameter(name = "loginId", description = "고객의 로그인 Id"),
		@Parameter(name = "nickname", description = "고객의 닉네임"),
		@Parameter(name = "phone", description = "고객의 전화번호"),
		@Parameter(name = "gender", description = "고객의 성별"),
		@Parameter(name = "ageRange",description = "고객의 나이"),
		@Parameter(name = "customerAddresses",description = "고객의 주소")
	})
	@GetMapping("/my")
	public Result<CustomerResponseDto> findCustomer();

	@Operation(summary = "마이페이지-나의 주소 조회", description = "사용자는 가입 시 입력한 주소를 조회할 수 있습니다.")
	@GetMapping("/my/addresses")
	public Result<List<CustomerAddressResponseDto>> findCustomerAddress();

	@Operation(summary = "마이페이지-개인정보 변경(기본 주소 변경)", description="사용자는 매장 조회와 배달에 사용할 기본 주소를 변경할 수 있습니다.")
	@PostMapping("/addresses")
	public Result<Void> updateDefaultAddress(@PathVariable Long addressId);

	@Operation(summary = "마이페이지-개인정보 변경(서브주소 추가)", description = "사용자는 매장 조회와 배달에 사용할 서브 주소를 추가 할 수 있습니다.")
	@PutMapping("/addresses/{addressId}")
	public Result<Void> addAddress(@Valid @RequestBody CustomerAddressRequestDto customerAddressRequestDto);

	@Operation(summary = "마이페이지-개인정보 변경(서브주소 변경)", description = "사용자는 매장 조회와 배달에 사용할 서브 주소를 변경 할 수 있습니다.")
	@PutMapping("/addresses/{addressId}")
	public Result<Void> updateAddress(@Valid @PathVariable Long addressId,
		@RequestBody CustomerAddressUpdateDto addressDto);

	@Operation(summary = "마이페이지-개인정보 변경(서브주소 삭제)", description = "사용자는 매장 조회와 배달에 사용할 서브 주소를 삭제 할 수 있습니다.")
	@DeleteMapping("/addresses/{addressId}")
	public Result<Void> deleteAddress(@PathVariable Long addressId);

	@Operation(summary = "마이페이지- 홀/ 포장 주문 내역 조회", description = "사용자는 마이페이지에서 홀 또는 포장 주문 내역을 조회 할 수 있습니다.")
	@GetMapping("/my/orders/hall")
	public Result<Page<HallOrderResponseDto>> getMyOrders(@PageableDefault(size = 10) Pageable pageable);

	@Operation(summary = "마이페이지- 배달 주문 내역 조회", description = "사용자는 마이페이지에서 배달 주문 내역을 조회 할 수 있습니다.")
	@GetMapping("/my/orders/delivery")
	public Result<Page<DeliveryOrderResponseDto>> getMyDeliveryOrders(@PageableDefault(size = 10) Pageable pageable);
}
