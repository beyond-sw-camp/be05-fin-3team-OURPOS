package com.ourpos.api.customer.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequestDto {

	private Long addressId;
	private Long customerId;
	private String addressName;
	private String addressSi;
	private String addressGu;
	private String streetName;
	private String addressDetail;
	private Boolean defaultYn;

	public CustomerRequestDto(Long addressId, Long customerId, String addressName, String addressSi, String addressGu, String streetName, String addressDetail, Boolean defaultYn) {
		this.addressId = addressId;
		this.customerId = customerId;
		this.addressName = addressName;
		this.addressSi = addressSi;
		this.addressGu = addressGu;
		this.streetName = streetName;
		this.addressDetail = addressDetail;
		this.defaultYn = defaultYn;
	}
}

