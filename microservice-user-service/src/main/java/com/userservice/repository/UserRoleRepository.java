package com.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.userservice.model.UserRole;


public interface UserRoleRepository extends MongoRepository<UserRole, Long> {
	
	public UserRole findByRoleName(String roleName);

}
