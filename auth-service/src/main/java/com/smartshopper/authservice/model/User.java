package com.smartshopper.authservice.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "users")
public class User {

	@Transient
	public static final String SEQUENCE_NAME = "user_sequence";

	@Id
	private long id;
	private String firstName;
	private String lastName;
	// private String userName;
	private String email;
	private String password;
	private long contactNumber;
	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime updatedAt;
//	@LastModifiedDate
	private LocalDateTime  lastLogin;
	private boolean accountStatus;
	@DBRef
	private List<Address> addresses = new ArrayList<>();
	@DBRef
	private Set<UserRole> roles;

	
//    private Date accountDeactivationDate;
//    private String accountDeactivationReason;
//    private boolean emailVerified;
//    private boolean phoneNumberVerified;
}
