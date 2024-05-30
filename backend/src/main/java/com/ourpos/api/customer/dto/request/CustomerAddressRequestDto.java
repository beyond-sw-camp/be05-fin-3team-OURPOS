package com.ourpos.api.customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class CustomerAddressRequestDto {

    @NotBlank(message = "주소 이름을 입력해주세요")
    private String name;
    @NotBlank(message = "수령하실 분의 이름을 입력해주세요")
    private String receiverName;
    @NotBlank(message = "전화번호를 입력해주세요" )
    private String telNo;
    @NotNull(message = "도로명 주소를 입력해주세요")
    private String addressBase;
    @NotNull(message = "상세주소를 입력해주세요")
    private String addressDetail;
    @NotBlank(message = "우편주소를 입력해주세요")
    private String zipcode;

    @Builder
    public CustomerAddressRequestDto(String name, String receiverName, String telNo, String addressBase,
        String addressDetail, String zipcode) {
        this.name = name;
        this.receiverName = receiverName;
        this.telNo = telNo;
        this.addressBase = addressBase;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
    }
}
