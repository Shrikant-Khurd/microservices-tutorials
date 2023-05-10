package com.departmentservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.departmentservice.model.Department;
import com.departmentservice.service.DepartmentService;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	@PostMapping("/add-department")
	public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
		departmentService.addDepartmenet(department);
		return new ResponseEntity<Department>(department, HttpStatus.CREATED);
	}

	@GetMapping("/get-all-departments")
	public ResponseEntity<List<Department>> getAllDepartment() {
		List<Department> departmentResponses = departmentService.getAllDepartments();
		return new ResponseEntity<List<Department>>(departmentResponses, null, HttpStatus.CREATED);
	}

	@PostMapping("/update-department/{id}")
	public ResponseEntity<Department> updateDepartment(@PathVariable("id") long departmentId,
			@RequestBody Department department) {

		Department UpdatedDepartment =departmentService.updateDepartment(departmentId, department);
		return new ResponseEntity<Department>(UpdatedDepartment, HttpStatus.CREATED);
	}

	@GetMapping("/byId/{id}")
	public ResponseEntity<Department> getDepartment(@PathVariable("id") long departmentId) {
		Department department = departmentService.getDepartmentById(departmentId);
		return new ResponseEntity<Department>(department, HttpStatus.OK);
	}
	
	@GetMapping("/department-name/{departmentName}")
	public ResponseEntity<Department> getDepartmentByDepartmentName(@PathVariable("departmentName") String departmentName) {
		Department department = departmentService.getDepartmentByDepartmentName(departmentName);
		return new ResponseEntity<Department>(department, HttpStatus.OK);
	}
	
	@GetMapping("/department-address/{departmentAddress}")
	public ResponseEntity<List<Department>> getDepartmentByDepartmentAddress(@PathVariable("departmentAddress") String departmentAddress) {
		List<Department> department = departmentService.getDepartmentByDepartmentAddress(departmentAddress);
		return new ResponseEntity<List<Department>>(department, HttpStatus.OK);
	}

	@DeleteMapping("delete-department/{id}")
	public ResponseEntity<String> deleteDepartment(@PathVariable("id") long departmentId) {
		departmentService.deleteDepartment(departmentId);
		return new ResponseEntity<String>("Department delete successfully", HttpStatus.CREATED);

	}

}
