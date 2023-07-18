package com.mongodb.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.productservice.model.UserRole;


public interface UserRoleRepository extends MongoRepository<UserRole, Long> {
	
	public UserRole findByRoleName(String roleName);

}
