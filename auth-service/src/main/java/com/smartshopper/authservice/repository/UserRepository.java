package com.smartshopper.authservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartshopper.authservice.model.User;


public interface UserRepository extends MongoRepository<User, Long> {
	
	List<User> findByRolesContaining(long id);
	public User findByEmail(String email); 
	public User findByContactNumber(long contactNumber); 
}
