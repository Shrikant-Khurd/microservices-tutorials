package com.departmentservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
//@Document(collection="departments")
@Entity
@Table(name="departments")
public class Department {
	
//	@Transient
//	public static final String SEQUENCE_NAME="department_sequence";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long departmentId;
	private String departmentName;
	private String departmentAddress;
	private String departmentCode;

}

