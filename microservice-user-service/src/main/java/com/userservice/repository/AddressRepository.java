package com.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.userservice.model.Address;

public interface AddressRepository extends MongoRepository<Address, Long> {

}
