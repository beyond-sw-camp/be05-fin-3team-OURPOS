package com.ourpos.domain.customer;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class CustomerAddressRepositoryTest {

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @DisplayName("고객의 주소를 조회할 수 있다.")
    @Test
    void findByCustomer() {
        // given
        CustomerAddress customerAddress1 = createCustomerAddress("집", "경기도 군포시 당정동 금정로 1번길",
            "아파트 1층 101호");
        CustomerAddress customerAddress2 = createCustomerAddress("회사", "서울시 강남구 강남로 1번길",
            "아파트 2층 201호");
        Customer customer = createCustomer("로그인 ID", List.of(customerAddress1, customerAddress2));
        customerRepository.save(customer);

        // when
        List<CustomerAddress> customerAddresses = customerAddressRepository.findByCustomer(customer);

        // then
        assertThat(customerAddresses).hasSize(2)
            .extracting("name", "receiverName", "telNo", "addressBase", "addressDetail")
            .containsExactlyInAnyOrder(
                tuple("집", "홍길동", "02-1234-5678", "경기도 군포시 당정동 금정로 1번길", "아파트 1층 101호"),
                tuple("회사", "홍길동", "02-1234-5678", "서울시 강남구 강남로 1번길", "아파트 2층 201호")
            );
    }

    private static CustomerAddress createCustomerAddress(String name, String addressBase, String addressDetail) {
        return CustomerAddress.builder()
            .name(name)
            .receiverName("홍길동")
            .telNo("02-1234-5678")
            .addressBase(addressBase)
            .addressDetail(addressDetail)
            .build();
    }

    private static Customer createCustomer(String loginId, List<CustomerAddress> customerAddresses) {
        return Customer.builder()
            .loginId(loginId)
            .customerAddresses(customerAddresses)
            .name("홍길동")
            .phone("010-1234-5678")
            .role(Role.ROLE_USER)
            .nickname("닉네임")
            .build();
    }

}