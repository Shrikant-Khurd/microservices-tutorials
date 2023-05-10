package com.departmentservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.departmentservice.model.Department;

//public interface DepartmentRepository extends MongoRepository<Department, Long> {
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	//public Department findByDepartmentId(long departmentId);
	
	public Department findByDepartmentName(String departmentName);
	
	public List<Department> findByDepartmentAddress(String departmentAddress);

}
