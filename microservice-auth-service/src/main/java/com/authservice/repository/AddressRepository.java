package com.authservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.authservice.model.Address;

public interface AddressRepository extends MongoRepository<Address, Long> {

}
