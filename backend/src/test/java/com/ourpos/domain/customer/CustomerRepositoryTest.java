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
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @DisplayName("고객은 로그인 ID로 조회할 수 있다.")
    @Test
    void findByLoginId() {
        // given
        String loginId = "로그인 ID";
        Customer customer = createCustomer(loginId);

        // when
        customerRepository.save(customer);
        Customer findCustomer = customerRepository.findByLoginId(loginId).get();

        // then
        assertThat(findCustomer).isEqualTo(customer);
    }

    @DisplayName("고객은 회원가입을 할 수 있다.")
    @Test
    void register() {
        CustomerAddress customerAddress1 = createCustomerAddress("집", "경기도 군포시 당정동 금정로 1번길",
            "아파트 1층 101호");

        CustomerAddress customerAddress2 = createCustomerAddress("회사", "서울시 강남구 강남로 1번길",
            "아파트 2층 201호");

        Customer customer = createCustomer("로그인 ID");

        Customer admin = Customer.builder()
            .loginId("로그인 ID2")
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

    @DisplayName("고객은 여러 주소를 저장해 둘 수 있다.")
    @Test
    void saveCustomerAddress() {
        // given
        CustomerAddress customerAddress1 = createCustomerAddress("집", "경기도 군포시 당정동 금정로 1번길",
            "아파트 1층 101호");

        CustomerAddress customerAddress2 = createCustomerAddress("회사", "서울시 강남구 강남로 1번길",
            "아파트 2층 201호");

        Customer customer = Customer.builder()
            .loginId("로그인 ID")
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

    private CustomerAddress createCustomerAddress(String name, String addressBase, String addressDetail) {
        return CustomerAddress.builder()
            .name(name)
            .receiverName("홍길동")
            .telNo("02-1234-5678")
            .addressBase(addressBase)
            .addressDetail(addressDetail)
            .build();
    }

    private Customer createCustomer(String loginId) {
        return Customer.builder()
            .loginId(loginId)
            .name("홍길동")
            .phone("010-1234-5678")
            .role(Role.ROLE_USER)
            .nickname("닉네임")
            .build();
    }
}