package com.ourpos.domain.customer;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("SELECT c"
		+ " FROM Customer c"
		+ " WHERE c.Id = :Id")
	Optional<Customer> findByCustomerId(Long Id);

	@Query("SELECT a"
		+" FROM CustomerAddress a"
		+" WHERE a.id = :addressId")
	Optional<CustomerAddress> findAddressById(Long addressId);




}
