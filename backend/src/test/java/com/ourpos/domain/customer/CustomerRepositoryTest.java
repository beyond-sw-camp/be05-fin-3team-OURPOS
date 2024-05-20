package com.ourpos.domain.customer;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Rollback(false)
    @DisplayName("고객은 회원가입을 할 수 있다.")
    @Test
    void register() {
        CustomerAddress customerAddress1 = CustomerAddress.builder()
            .name("집")
            .addressSi("군포시")
            .addressGu("당정동")
            .streetName("금정로 1번길")
            .addressDetail("아파트 1층 101호")
            .defaultYn(true)
            .build();

        CustomerAddress customerAddress2 = CustomerAddress.builder()
            .name("회사")
            .addressSi("서울시")
            .addressGu("강남구")
            .streetName("강남로 1번길")
            .addressDetail("아파트 2층 201호")
            .defaultYn(false)
            .build();

        Customer customer = Customer.builder()
            .loginId("로그인 ID")
            .password("비밀번호")
            .name("홍길동")
            .phone("010-1234-5678")
            .role(Role.ROLE_USER)
            .nickname("닉네임")
            .build();

        Customer admin = Customer.builder()
            .loginId("로그인 ID2")
            .password("비밀번호2")
            .name("홍길동2")
            .phone("010-1234-5678")
            .role(Role.ROLE_ADMIN)
            .nickname("닉네임2")
            .customerAddress(customerAddress1)
            .customerAddress(customerAddress2)
            .build();
        customerRepository.saveAll(List.of(customer, admin));

        // when
        List<Customer> customers = customerRepository.findAll();

        // then
        assertThat(customers).hasSize(2)
            .extracting("loginId", "role")
            .containsExactlyInAnyOrder(
                tuple("로그인 ID", Role.ROLE_USER),
                tuple("로그인 ID2", Role.ROLE_ADMIN)
            );
    }

    @Rollback(false)
    @DisplayName("고객은 여러 주소를 저장해 둘 수 있다.")
    @Test
    void saveCustomerAddress() {
        // given
        CustomerAddress customerAddress1 = CustomerAddress.builder()
            .name("집")
            .addressSi("군포시")
            .addressGu("당정동")
            .streetName("금정로 1번길")
            .addressDetail("아파트 1층 101호")
            .defaultYn(true)
            .build();

        CustomerAddress customerAddress2 = CustomerAddress.builder()
            .name("회사")
            .addressSi("서울시")
            .addressGu("강남구")
            .streetName("강남로 1번길")
            .addressDetail("아파트 2층 201호")
            .defaultYn(false)
            .build();

        Customer customer = Customer.builder()
            .loginId("로그인 ID")
            .password("비밀번호")
            .name("홍길동")
            .phone("010-1234-5678")
            .role(Role.ROLE_USER)
            .customerAddress(customerAddress1)
            .customerAddress(customerAddress2)
            .nickname("닉네임")
            .build();

        // when
        customerRepository.save(customer);

        // then
        assertThat(customer.getCustomerAddresses()).hasSize(2);
    }
}