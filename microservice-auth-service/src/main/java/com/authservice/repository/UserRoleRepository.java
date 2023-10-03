package com.authservice.repository;

import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.authservice.model.UserRole;


public interface UserRoleRepository extends MongoRepository<UserRole, Long> {
	
	public UserRole findByRoleName(String roleName);



	public Set<UserRole> findByRoleId(long roleId);
}
