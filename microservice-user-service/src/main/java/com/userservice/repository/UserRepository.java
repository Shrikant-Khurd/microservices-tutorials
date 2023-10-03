package com.userservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.userservice.model.User;


public interface UserRepository extends MongoRepository<User, Long> {
	
	List<User> findByRolesContaining(long id);
	public User findByEmail(String email); 
//	public User findByUserName(String userName); 
}
