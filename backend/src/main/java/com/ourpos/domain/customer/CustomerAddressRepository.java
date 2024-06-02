package com.ourpos.domain.customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface
CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

    List<CustomerAddress> findByCustomer(Customer customer);
}
