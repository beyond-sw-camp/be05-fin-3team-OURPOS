package com.ourpos.api.customer.dto.response;

import com.ourpos.domain.customer.CustomerAddress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerAddressResponseDto {

	private Long id;
	private String name;
	private String addressSi;
	private String addressGu;
	private String streetName;
	private String addressDetail;
	private Boolean defaultYn;

	public CustomerAddressResponseDto(CustomerAddress customerAddress) {
		this.id = customerAddress.getId();
		this.name = customerAddress.getName();
		this.addressSi = customerAddress.getAddressSi();
		this.addressGu = customerAddress.getAddressGu();
		this.streetName = customerAddress.getStreetName();
		this.addressDetail = customerAddress.getAddressDetail();
		this.defaultYn = customerAddress.getDefaultYn();
	}
}
