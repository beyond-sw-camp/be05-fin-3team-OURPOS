package com.ourpos.api.customer.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerAddressUpdateDto {

	private String addressSi;
	private String addressGu;
	private String streetName;
	private String addressDetail;
}
