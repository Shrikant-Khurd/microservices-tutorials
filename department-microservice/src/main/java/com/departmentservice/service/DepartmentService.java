package com.departmentservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.departmentservice.controller.DepartmentController;
import com.departmentservice.model.Department;
import com.departmentservice.repository.DepartmentRepository;
import com.departmentservice.util.SequenceGenaratorService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	public Department addDepartmenet(Department department) {
		log.info("add department method called");
		log.info("department is added");
		return departmentRepository.save(department);
	}	
	
	public Department updateDepartment(long departmentId, Department department) {
		log.info("update department method called");
		Department updateDepartment=departmentRepository.findById(departmentId).get();
		updateDepartment.setDepartmentName(department.getDepartmentName());
		updateDepartment.setDepartmentAddress(department.getDepartmentAddress());
		updateDepartment.setDepartmentCode(department.getDepartmentCode());
		log.info("department is updated");
		return departmentRepository.save(updateDepartment);
	}

	public Department getDepartmentById(long departmentId) {
		log.info("get department details method called");
		return departmentRepository.findById(departmentId).get();	
	}

	public void deleteDepartment(long departmentId) {
		log.info("delete department method called");
	   departmentRepository.deleteById(departmentId);	
	}

	public List<Department> getAllDepartments() {
		log.info("get all department method called");
		return departmentRepository.findAll();
	}

	public Department getDepartmentByDepartmentName(String departmentName) {
		return  departmentRepository.findByDepartmentName(departmentName);	
	}

	public List<Department> getDepartmentByDepartmentAddress(String departmentAddress) {
		return  departmentRepository.findByDepartmentAddress(departmentAddress);
	}

}
