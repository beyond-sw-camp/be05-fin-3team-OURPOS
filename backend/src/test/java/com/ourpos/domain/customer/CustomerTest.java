package com.ourpos.domain.customer;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @DisplayName("고객 이름, 닉네임, 휴대폰번호, 성별, 나이대, 프로필 이미지를 수정할 수 있다.")
    @Test
    void update() {
        // given
        Customer customer = createCustomer("홍길동", "010-1234-5678", "M", "30-39", "profile.jpg");

        // when
        customer.update("홍길순", "길순", "010-1234-5679", "F", "30-39", "profile2.jpg");

        // then
        assertThat(customer).extracting(
                "name", "nickname", "phone", "gender", "ageRange", "profileImage")
            .containsExactly("홍길순", "길순", "010-1234-5679", "F", "30-39", "profile2.jpg");
    }

    @DisplayName("고객은 최대 3개의 주소를 저장해 둘 수 있다.")
    @Test
    void addAddress() {
        // given
        Customer customer = createCustomer("홍길동", "010-1234-5678", "M", "30-39", "profile.jpg");
        CustomerAddress customerAddress = createCustomerAddress("집", "경기도 군포시 당정동 금정로 1번길", "아파트 1층 101호",
            "홍길동", "02-1234-5678");

        // when
        customer.addAddress(customerAddress);

        // then
        assertThat(customer.getCustomerAddresses()).hasSize(1)
            .extracting("name", "receiverName", "telNo", "addressBase", "addressDetail", "defaultYn")
            .containsExactlyInAnyOrder(
                tuple("집", "홍길동", "02-1234-5678", "경기도 군포시 당정동 금정로 1번길", "아파트 1층 101호", true)
            );
    }

    @DisplayName("3개 이상의 주소를 추가하려고 하면 예외가 발생한다.")
    @Test
    void addAddressEx() {
        // given
        Customer customer = createCustomer("홍길동", "010-1234-5678", "M", "30-39", "profile1.jpg");

        CustomerAddress customerAddress1 = createCustomerAddress("집", "경기도 군포시 당정동 금정로 1번길",
            "아파트 1층 101호", "홍길순", "02-1234-5678");

        CustomerAddress customerAddress2 = createCustomerAddress("회사", "서울시 강남구 강남로 1번길",
            "아파트 2층 201호", "홍길동", "02-1234-5678");

        CustomerAddress customerAddress3 = createCustomerAddress("학교", "서울시 강북구 강북로 1번길",
            "아파트 3층 301호", "홍길동", "02-1234-5678");

        CustomerAddress customerAddress4 = createCustomerAddress("기타", "서울시 강동구 강동로 1번길",
            "아파트 4층 401호", "홍길동", "02-1234-5678");

        // when
        customer.addAddress(customerAddress1);
        customer.addAddress(customerAddress2);
        customer.addAddress(customerAddress3);

        // then
        assertThatThrownBy(() -> customer.addAddress(customerAddress4))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("주소는 최대 3개까지 추가 가능합니다.");
    }

    @DisplayName("고객은 주소를 수정할 수 있다.")
    @Test
    void updateAddress() {
        // given
        Customer customer = createCustomer("홍길자", "010-1234-567", "F", "20-29", "profile.jpg");

        CustomerAddress customerAddress = createCustomerAddress("집", "경기도 군포시 당정동 금정로 1번길",
            "아파트 1층 101호", "홍길동", "010-1234-5678");

        customer.addAddress(customerAddress);

        // when
        customerAddress.update("회사", "홍길순", "02-1234-5679", "경기도 군포시 당정동 금정로 2번길", "아파트 2층 201호", "12345");

        // then
        assertThat(customer.getCustomerAddresses()).hasSize(1)
            .extracting("name", "receiverName", "telNo", "addressBase", "addressDetail", "zipcode", "defaultYn")
            .containsExactlyInAnyOrder(
                tuple("회사", "홍길순", "02-1234-5679", "경기도 군포시 당정동 금정로 2번길", "아파트 2층 201호", "12345", true)
            );
    }

    @DisplayName("고객 주소를 추가할 때 기본 주소가 하나도 없는 상황에서는 추가한 주소를 기본 주소로 설정한다.")
    @Test
    void customerAddressDefaultYn() {
        // given
        Customer customer = createCustomer("홍길동", "010-1234-5678", "M", "30-39", "profile.jpg");
        CustomerAddress customerAddress = createCustomerAddress("집", "경기도 군포시 당정동 금정로 1번길", "아파트 1층 101호",
            "홍길동", "02-1234-5678");

        // when
        customer.addAddress(customerAddress);

        // then
        assertThat(customer.getCustomerAddresses()).hasSize(1)
            .extracting("defaultYn")
            .containsExactly(true);
    }

    @DisplayName("고객 주소를 추가할 때 기본 주소가 이미 있는 상황에서는 추가한 주소를 기본 주소로 설정하지 않는다.")
    @Test
    void customerAddressDefaultYnEx() {
        // given
        Customer customer = createCustomer("홍길동", "010-1234-5678", "M", "30-39", "profile.jpg");

        CustomerAddress customerAddress1 = createCustomerAddress("집", "경기도 군포시 당정동 금정로 1번길", "아파트 1층 101호",
            "홍길동", "02-1234-5678");
        CustomerAddress customerAddress2 = createCustomerAddress("회사", "서울시 강남구 강남로 1번길", "아파트 2층 201호",
            "홍길동", "02-1234-5678");

        // when
        customer.addAddress(customerAddress1);
        customer.addAddress(customerAddress2);

        // then
        assertThat(customer.getCustomerAddresses()).hasSize(2)
            .extracting("defaultYn")
            .containsExactly(true, false);
    }

    @DisplayName("슈퍼관리자는 가게 사장님 역할을 관리자(ROLE_ADMIN)로 등록/변경 할 수 있다.")
    @Test
    void enrollAdmin() {
        // given
        Customer customer = createCustomer("홍길동", "010-1234-5678", "M", "30-39", "profile.jpg");

        // when
        customer.enrollAdmin();

        // then
        assertThat(customer.getRole()).isEqualTo(Role.ROLE_ADMIN);
    }

    @DisplayName("슈퍼관리자는 라이더 검증이 끝난 라이더의 역할을 라이더(ROLE_RIDER)로 등록/변경 할 수 있다.")
    @Test
    void enrollRider() {
        // given
        Customer customer = createCustomer("홍길동", "010-1234-5678", "M", "30-39", "profile.jpg");

        // when
        customer.enrollRider();

        // then
        assertThat(customer.getRole()).isEqualTo(Role.ROLE_RIDER);
    }

    @DisplayName("슈퍼관리자는 일반 사용자의 역할을 사용자(ROLE_USER)로 등록/변경 할 수 있다.")
    @Test
    void enrollUser() {
        // given
        Customer customer = createCustomer("홍길동", "010-1234-5678", "M", "30-39", "profile.jpg");

        // when
        customer.enrollUser();

        // then
        assertThat(customer.getRole()).isEqualTo(Role.ROLE_USER);
    }

    @DisplayName("고객의 주소에서 기본 주소를 다른 주소로 변경하면 변경된 주소가 기본 주소로 설정된다.")
    @Test
    void updateDefaultAddress() {
        // given
        Customer customer = createCustomer("홍길동", "010-1234-5678", "M", "30-39", "profile.jpg");

        CustomerAddress customerAddress1 = createCustomerAddress("집", "경기도 군포시 당정동 금정로 1번길", "아파트 1층 101호",
            "홍길동", "02-1234-5678");
        CustomerAddress customerAddress2 = createCustomerAddress("회사", "서울시 강남구 강남로 1번길", "아파트 2층 201호",
            "홍길동", "02-1234-5678");
        CustomerAddress customerAddress3 = createCustomerAddress("학교", "서울시 강북구 강북로 1번길", "아파트 3층 301호",
            "홍길동", "02-1234-5678");

        // when
        customer.addAddress(customerAddress1);
        customer.addAddress(customerAddress2);
        customer.addAddress(customerAddress3);

        assertThat(customer.getCustomerAddresses()).hasSize(3)
            .extracting("defaultYn")
            .containsExactly(true, false, false);
        customer.updateDefaultAddress(customerAddress2);

        // then
        assertThat(customer.getCustomerAddresses()).hasSize(3)
            .extracting("defaultYn")
            .containsExactly(false, true, false);
    }

    @DisplayName("고객의 주소가 1개일 때는 기본 주소를 변경할 수 없다.")
    @Test
    void updateDefaultAddressEx() {
        // given
        Customer customer = createCustomer("홍길동", "010-1234-5678", "M", "30-39", "profile.jpg");

        CustomerAddress customerAddress = createCustomerAddress("집", "경기도 군포시 당정동 금정로 1번길", "아파트 1층 101호", "홍길동",
            "02-1234-5678");

        // when
        customer.addAddress(customerAddress);

        // then
        assertThatThrownBy(() -> customer.updateDefaultAddress(customerAddress))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("주소가 1개일 때는 기본 주소를 변경할 수 없습니다.");
    }

    private CustomerAddress createCustomerAddress(String name, String addressBase, String addressDetail,
        String receiverName, String telNo) {
        return CustomerAddress.builder()
            .name(name)
            .addressBase(addressBase)
            .addressDetail(addressDetail)
            .receiverName(receiverName)
            .telNo(telNo)
            .build();
    }

    private Customer createCustomer(String name, String phone, String gender, String ageRange,
        String profileImage) {
        return Customer.builder()
            .name(name)
            .phone(phone)
            .gender(gender)
            .ageRange(ageRange)
            .profileImage(profileImage)
            .build();
    }
}