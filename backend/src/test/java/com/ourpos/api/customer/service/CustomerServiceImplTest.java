package com.ourpos.api.customer.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.customer.dto.request.CustomerAddressRequestDto;
import com.ourpos.api.customer.dto.request.CustomerAddressUpdateDto;
import com.ourpos.api.customer.dto.response.CustomerAddressResponseDto;
import com.ourpos.api.customer.dto.response.CustomerResponseDto;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerAddress;
import com.ourpos.domain.customer.CustomerRepository;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class CustomerServiceImplTest {

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @DisplayName("로그인 ID로 고객 정보를 조회할 수 있다.")
    @Test
    void findLoginCustomer() {
        // given
        Customer customer = Customer.builder()
            .loginId("naver_login")
            .build();
        customerRepository.save(customer);

        // when
        CustomerResponseDto customerResponseDto = customerService.findLoginCustomer("naver_login");

        // then
        assertThat(customerResponseDto.getLoginId()).isEqualTo("naver_login");
    }

    @DisplayName("로그인 ID로 고객 주소를 조회할 수 있다.")
    @Test
    void findLoginCustomerAddresses() {
        // given
        CustomerAddress customerAddress1 = CustomerAddress.builder()
            .name("집")
            .build();

        CustomerAddress customerAddress2 = CustomerAddress.builder()
            .name("회사")
            .build();

        Customer customer = Customer.builder()
            .loginId("naver_login")
            .customerAddress(customerAddress1)
            .customerAddress(customerAddress2)
            .build();
        customerRepository.save(customer);

        // when
        List<CustomerAddressResponseDto> customerAddresses = customerService.findLoginCustomerAddresses("naver_login");

        // then
        assertThat(customerAddresses).hasSize(2)
            .extracting("name")
            .containsExactlyInAnyOrder("집", "회사");
    }

    @DisplayName("로그인 고객의 주소를 추가할 수 있다.")
    @Test
    void addSubAddress() {
        // given
        String loginId = "naver_login";

        Customer customer = Customer.builder()
            .loginId(loginId)
            .build();
        customerRepository.save(customer);

        CustomerAddressRequestDto customerAddressRequest1 = CustomerAddressRequestDto.builder()
            .name("집")
            .build();
        CustomerAddressRequestDto customerAddressRequest2 = CustomerAddressRequestDto.builder()
            .name("회사")
            .build();
        customerService.addSubAddress(loginId, customerAddressRequest1);
        customerService.addSubAddress(loginId, customerAddressRequest2);

        // when
        List<CustomerAddressResponseDto> customerAddresses = customerService.findLoginCustomerAddresses(loginId);

        // then
        assertThat(customerAddresses).hasSize(2)
            .extracting("name", "defaultYn")
            .containsExactlyInAnyOrder(
                tuple("집", true),
                tuple("회사", false)
            );
    }

    @DisplayName("로그인 고객의 주소를 수정할 수 있다.")
    @Test
    void updateAddress() {
        // given
        String loginId = "naver_login";

        CustomerAddress customerAddress1 = CustomerAddress.builder()
            .name("집")
            .receiverName("김철수")
            .telNo("010-1234-1234")
            .addressBase("서울시 강동구")
            .addressDetail("역삼동")
            .zipcode("12323")
            .build();

        CustomerAddress customerAddress2 = CustomerAddress.builder()
            .name("회사")
            .receiverName("김영희")
            .telNo("010-1234-5678")
            .addressBase("서울시 강남구")
            .addressDetail("상수동")
            .zipcode("11224")
            .build();

        Customer customer = Customer.builder()
            .loginId(loginId)
            .customerAddress(customerAddress1)
            .customerAddress(customerAddress2)
            .build();
        customerRepository.save(customer);

        CustomerAddressUpdateDto addressUpdateDto = CustomerAddressUpdateDto.builder()
            .name("집집")
            .receiverName("홍길동")
            .telNo("010-1234-5678")
            .addressBase("서울시 강남구")
            .addressDetail("역삼동")
            .zipcode("12345")
            .build();

        // when
        customerService.updateAddress(customerAddress1.getId(), addressUpdateDto);
        List<CustomerAddressResponseDto> customerAddresses = customerService.findLoginCustomerAddresses(loginId);

        // then
        assertThat(customerAddresses).hasSize(2)
            .extracting("name", "receiverName", "telNo", "addressBase", "addressDetail", "zipcode")
            .containsExactlyInAnyOrder(
                tuple("집집", "홍길동", "010-1234-5678", "서울시 강남구", "역삼동", "12345"),
                tuple("회사", "김영희", "010-1234-5678", "서울시 강남구", "상수동", "11224")
            );
    }

    @DisplayName("로그인 고객의 주소를 삭제할 수 있다.")
    @Test
    void deleteAddress() {
        // given
        String loginId = "naver_login";

        CustomerAddress customerAddress1 = CustomerAddress.builder()
            .name("집")
            .receiverName("김철수")
            .telNo("010-1234-1234")
            .addressBase("서울시 강동구")
            .addressDetail("역삼동")
            .zipcode("12323")
            .build();

        CustomerAddress customerAddress2 = CustomerAddress.builder()
            .name("회사")
            .receiverName("김영희")
            .telNo("010-1234-5678")
            .addressBase("서울시 강남구")
            .addressDetail("상수동")
            .zipcode("11224")
            .build();

        Customer customer = Customer.builder()
            .loginId(loginId)
            .customerAddress(customerAddress1)
            .customerAddress(customerAddress2)
            .build();
        customerRepository.save(customer);

        // when
        customerService.deleteAddress(customerAddress2.getId());

        List<CustomerAddressResponseDto> customerAddresses = customerService.findLoginCustomerAddresses(loginId);

        // then
        assertThat(customerAddresses).hasSize(1)
            .extracting("name")
            .containsExactly("집");
    }

    @DisplayName("로그인 고객의 기본 주소는 삭제할 수 없다.")
    @Test
    void test() {
        // given
        String loginId = "naver_login";

        CustomerAddress customerAddress1 = CustomerAddress.builder()
            .name("집")
            .receiverName("김철수")
            .telNo("010-1234-1234")
            .addressBase("서울시 강동구")
            .addressDetail("역삼동")
            .zipcode("12323")
            .build();

        CustomerAddress customerAddress2 = CustomerAddress.builder()
            .name("회사")
            .receiverName("김영희")
            .telNo("010-1234-5678")
            .addressBase("서울시 강남구")
            .addressDetail("상수동")
            .zipcode("11224")
            .build();

        Customer customer = Customer.builder()
            .loginId(loginId)
            .customerAddress(customerAddress1)
            .customerAddress(customerAddress2)
            .build();

        // when
        customerRepository.save(customer);
        Long customerAddress1Id = customerAddress1.getId();

        // then
        assertThatThrownBy(() -> {
            customerService.deleteAddress(customerAddress1Id);
        })
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("기본 주소는 삭제할 수 없습니다.");
    }

    @DisplayName("로그인 고객의 기본 주소를 변경할 수 있다.")
    @Test
    void updateDefaultAddress() {
        // given
        String loginId = "naver_login";

        CustomerAddress customerAddress1 = CustomerAddress.builder()
            .name("집")
            .receiverName("김철수")
            .telNo("010-1234-1234")
            .addressBase("서울시 강동구")
            .addressDetail("역삼동")
            .zipcode("12323")
            .build();

        CustomerAddress customerAddress2 = CustomerAddress.builder()
            .name("회사")
            .receiverName("김영희")
            .telNo("010-1234-5678")
            .addressBase("서울시 강남구")
            .addressDetail("상수동")
            .zipcode("11224")
            .build();

        Customer customer = Customer.builder()
            .loginId(loginId)
            .customerAddress(customerAddress1)
            .customerAddress(customerAddress2)
            .build();
        customerRepository.save(customer);

        // when
        List<CustomerAddressResponseDto> beforeCustomerAddresses = customerService.findLoginCustomerAddresses(loginId);
        assertThat(beforeCustomerAddresses).hasSize(2)
            .extracting("name", "defaultYn")
            .containsExactlyInAnyOrder(
                tuple("집", true),
                tuple("회사", false)
            );
        customerService.updateDefaultAddress(loginId, customerAddress2.getId());
        List<CustomerAddressResponseDto> AfterCustomerAddresses = customerService.findLoginCustomerAddresses(loginId);

        // then
        assertThat(AfterCustomerAddresses).hasSize(2)
            .extracting("name", "defaultYn")
            .containsExactlyInAnyOrder(
                tuple("집", false),
                tuple("회사", true)
            );
    }

    @DisplayName("로그인 고객의 주소가 1개일 때는 기본 주소를 변경할 수 없다.")
    @Test
    void updateDefaultAddressEx() {
        // given
        String loginId = "naver_login";

        CustomerAddress customerAddress = CustomerAddress.builder()
            .name("집")
            .receiverName("김철수")
            .telNo("010-1234-1234")
            .addressBase("서울시 강동구")
            .addressDetail("역삼동")
            .zipcode("12323")
            .build();

        Customer customer = Customer.builder()
            .loginId(loginId)
            .customerAddress(customerAddress)
            .build();
        customerRepository.save(customer);

        // when
        Long customerAddressId = customerAddress.getId();

        // then
        assertThatThrownBy(() -> {
            customerService.updateDefaultAddress(loginId, customerAddressId);
        })
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("주소가 1개일 때는 기본 주소를 변경할 수 없습니다.");
    }

}