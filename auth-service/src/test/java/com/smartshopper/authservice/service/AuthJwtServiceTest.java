package com.smartshopper.authservice.service;

import com.smartshopper.authservice.dto.LoginRequest;
import com.smartshopper.authservice.dto.LoginResponse;
import com.smartshopper.authservice.dto.UserDto;
import com.smartshopper.authservice.dto.UserRoleDto;
import com.smartshopper.authservice.exception.ResourceNotFoundException;
import com.smartshopper.authservice.mapper.UserRoleMapper;
import com.smartshopper.authservice.model.User;
import com.smartshopper.authservice.model.UserRole;
import com.smartshopper.authservice.repository.UserRepository;
import com.smartshopper.authservice.repository.UserRoleRepository;
import com.smartshopper.authservice.security.JwtFilter;
import com.smartshopper.authservice.security.JwtTokenProvider;
import com.smartshopper.authservice.utils.SequenceGenaratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest
class AuthJwtServiceTest {
    @Mock
    private SequenceGenaratorService sequenceGeneratorService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtUtil;
    @InjectMocks
    private AuthJwtService authService;

    @BeforeEach
    void setUp() {
    }

//    @Test
//    void testGetUserByEmail() {
//        String email = "shrikantkhurd@gmail.com";
//        User expectedUser = new User();
//        expectedUser.setEmail(email);
//        when(userRepository.findByEmail(email)).thenReturn(expectedUser);
//        User resultUser = authService.loadUserByEmail(email);
//        assertNotNull(resultUser);
//        assertEquals(expectedUser, resultUser);
//    }

    @Test
    void testLoadUserByEmail() {
        String email = "hrikantkhurd@gmail.com";
        User expectedUser = new User();
        expectedUser.setEmail("hrikantkhurd@gmail.com");
        expectedUser.setPassword("hashed_password");
        expectedUser.setContactNumber(1234567890L);
        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        User loadedUser = authService.loadUserByEmail(email);
        System.out.println(expectedUser.getEmail() + " " + loadedUser.getEmail());
        System.out.println(loadedUser);
        System.out.println(expectedUser);
        assertNotNull(loadedUser);
        assertEquals(email, loadedUser.getEmail());
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        String email = "shrikantkhurd@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(null);
        User resultUser = authService.loadUserByEmail(email);
        assertNull(resultUser);
    }
    
    @Test
    void testCreateJwtTokenWithEmailSuccess() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hashed_password");
        when(userRepository.findByEmail(loginRequest.getEmailOrContactNumber())).thenReturn(user);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateJwtToken(user)).thenReturn("jwt_token");

        LoginResponse response = authService.createJwtToken(loginRequest);
        System.out.println(response);
        assertNotNull(response);
        assertEquals("jwt_token", response.getAccessToken());
    }

    @Test
    void testCreateJwtTokenWithEmailNotFound() {
        LoginRequest loginRequest = new LoginRequest("nonexistent@example.com", "password");
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> authService.createJwtToken(loginRequest));
        assertEquals("Email not found, please enter valid email.", exception.getMessage());
    }

    @Test
    void testCreateJwtTokenWithContactNumberSuccess() {
        LoginRequest loginRequest = new LoginRequest("1234567890", "password");
        User user = new User();
        user.setContactNumber(1234567890L);
        user.setPassword("hashed_password");
        when(userRepository.findByContactNumber(Long.parseLong(loginRequest.getEmailOrContactNumber()))).thenReturn(user);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateJwtToken(user)).thenReturn("jwt_token");

        LoginResponse response = authService.createJwtToken(loginRequest);

        assertNotNull(response);
        assertEquals("jwt_token", response.getAccessToken());
    }

    @Test
    void testCreateJwtTokenWithContactNumberNotFound() {
        long contactNumber = 9876543210L;
        LoginRequest loginRequest = new LoginRequest("9876543210", "password");
        when(userRepository.findByContactNumber(contactNumber)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> authService.createJwtToken(loginRequest));
        assertEquals("Mobile number not found, please enter valid mobile number.", exception.getMessage());
    }

    @Test
    void testCreateJwtTokenInvalidEmail() {
        String email = "invalid_email@gmail.com";
        LoginRequest loginRequest = new LoginRequest("invalid_email@gmail.com", "password");
//        when(userRepository.findByEmail(anyString())).thenReturn(null);
//        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()
//                -> authService.createJwtToken(loginRequest));
//        System.out.println("Exception msg : - "+ exception.getMessage());
//        assertEquals("Email not found, please enter valid email.", exception.getMessage());

        when(userRepository.findByEmail(email)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()
                -> authService.createJwtToken(loginRequest));
        assertEquals("Email not found, please enter valid email.", exception.getMessage());
    }

    @Test
    void testCreateJwtTokenIncorrectPassword() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrong_password");
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hashed_password");
        when(userRepository.findByEmail(loginRequest.getEmailOrContactNumber())).thenReturn(user);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> authService.createJwtToken(loginRequest));
        assertEquals("Incorrect password, please enter correct password.", exception.getMessage());
    }



    @Test
    void testLoadUserByContactNumber() {
        long contactNumber = 1234567890L;
        User expectedUser = new User();
        expectedUser.setEmail("test@example.com");
        expectedUser.setPassword("hashed_password");
        expectedUser.setContactNumber(1234567890L);

        when(userRepository.findByContactNumber(contactNumber)).thenReturn(expectedUser);

        User loadedUser = authService.loadUserByContactNumber(contactNumber);
        System.out.println(loadedUser);
        System.out.println(expectedUser);
        assertNotNull(loadedUser);
        assertEquals(expectedUser, loadedUser);
    }

    //Role test cases
    @Test
    void testGetCurrentUserRole() {
        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(1L, "ROLE_USER");
        userRoles.add(userRole);

        when(JwtFilter.CURRENT_USER_ROLE).thenReturn(userRoles);

        UserRole currentUserRole = authService.currectUserRole();

        assertEquals(userRole, currentUserRole);
    }

    @Test
    void testGetUsersByRoleName() {
        String roleName = "ROLE_ADMIN";
        UserRole role = new UserRole(1L, roleName);
        List<User> userListByRole = List.of(new User(),new User(),new User());

        when(userRoleRepository.findByRoleName(roleName)).thenReturn(role);
        when(userRepository.findByRolesContaining(role.getRoleId())).thenReturn(userListByRole);

        List<UserDto> userList = authService.getUsersByRoleName(roleName);

        assertNotNull(userList);
        assertEquals(userListByRole.size(), userList.size());
    }

    @Test
    void testAddUserRole() {
        UserRoleDto roleDto = new UserRoleDto();
        roleDto.setRoleName("ROLE_USER");
        UserRole userRole = UserRoleMapper.mapToUserRole(roleDto);
        userRole.setRoleId(1L);
        when(sequenceGeneratorService.generateSequence(UserRole.SEQUENCE_NAME)).thenReturn(1L);
        when(userRoleRepository.save(any(UserRole.class))).thenReturn(userRole);

        UserRoleDto addedRole = authService.addUserRole(roleDto);

        assertNotNull(addedRole);
        assertEquals(userRole.getRoleId(), addedRole.getId());
    }

    @Test
    void testGetUsersByRoleId() {
        long roleId = 1;
        UserRole role = new UserRole(roleId, "ROLE_ADMIN");

        when(userRoleRepository.findById(roleId)).thenReturn(java.util.Optional.of(role));

        UserRole returnedRole = authService.getUsersByRoleId(roleId);

        assertNotNull(returnedRole);
        assertEquals(role, returnedRole);
    }

    @Test
    void testGetAllRoles() {
        List<UserRole> userRoles = List.of(new UserRole(1L, "ROLE_USER"), new UserRole(2L, "ROLE_ADMIN"));

        when(userRoleRepository.findAll()).thenReturn(userRoles);
        List<UserRoleDto> roleDtos = authService.getAllRoles();

        assertNotNull(roleDtos);
        assertEquals(userRoles.size(), roleDtos.size());
    }
}