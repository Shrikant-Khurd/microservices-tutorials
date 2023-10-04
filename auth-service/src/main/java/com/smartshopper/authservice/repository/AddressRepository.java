package com.smartshopper.authservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartshopper.authservice.model.Address;

public interface AddressRepository extends MongoRepository<Address, Long> {

}
