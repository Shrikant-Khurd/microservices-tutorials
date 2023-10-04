package com.smartshopper.authservice.repository;

import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartshopper.authservice.model.UserRole;


public interface UserRoleRepository extends MongoRepository<UserRole, Long> {
	
	public UserRole findByRoleName(String roleName);
	public Set<UserRole> findByRoleId(Long roleId);
}
