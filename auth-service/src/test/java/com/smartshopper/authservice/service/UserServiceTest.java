package com.smartshopper.authservice.service;

import com.smartshopper.authservice.config.MessageSourceConfig;
import com.smartshopper.authservice.dto.AddressDto;
import com.smartshopper.authservice.dto.ApiResponse;
import com.smartshopper.authservice.dto.UserDto;
import com.smartshopper.authservice.exception.EmailAlreadyExistException;
import com.smartshopper.authservice.exception.ResourceNotFoundException;
import com.smartshopper.authservice.mapper.AddressMapper;
import com.smartshopper.authservice.model.Address;
import com.smartshopper.authservice.model.User;
import com.smartshopper.authservice.model.UserRole;
import com.smartshopper.authservice.repository.AddressRepository;
import com.smartshopper.authservice.repository.UserRepository;
import com.smartshopper.authservice.repository.UserRoleRepository;
import com.smartshopper.authservice.utils.SequenceGenaratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@TestPropertySource("classpath:/messages/api_error_messages")
//@TestPropertySource("classpath:/messages/api_response_messages")
//@EnableConfigurationProperties(value = MessageSource.class)
@ContextConfiguration(classes = MessageSourceConfig.class)
//@SpringBootTest
class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final SequenceGenaratorService sequenceGeneratorService = mock(SequenceGenaratorService.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    private final UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
    private final AddressRepository addressRepository = mock(AddressRepository.class);
    private final MessageSource messageSource = mock(MessageSource.class);

    private final UserService userService = new UserService(userRepository, userRoleRepository, addressRepository,
            sequenceGeneratorService, messageSource, bCryptPasswordEncoder);


    @BeforeEach
    void setUp() {
    }

    @Test()
     void testAddUser_ThrowsEmailAlreadyExistException() {
        UserDto userDto = new UserDto();
        userDto.setEmail("shrikantkhurd@gmail.com");

        User existingUser = new User();
        existingUser.setEmail("shrikantkhurd@gmail.com");
        when(userRepository.findByEmail(anyString())).thenReturn(existingUser);
        when(userRepository.findByEmail(any())).thenReturn(existingUser);
        EmailAlreadyExistException exception = assertThrows(EmailAlreadyExistException.class, () -> userService.addUser(userDto));

        String expectedMessage = messageSource.getMessage("api.error.user.already.registered", null, Locale.ENGLISH);
        System.out.println(exception.getMessage());
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testAddUser_Success() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(null);

        User userDetails = new User();
        userDetails.setId(1L);
        userDetails.setCreatedAt(LocalDateTime.now());
        userDetails.setAccountStatus(true);
        when(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME)).thenReturn(1L);
        when(bCryptPasswordEncoder.encode(userDto.getPassword())).thenReturn("hashedPassword");

        UserRole userRole = new UserRole();
        userRole.setRoleId(2L);
        when(userRoleRepository.findByRoleId(2L)).thenReturn(Collections.singleton(userRole));


        List<Address> addresses = new ArrayList<>();
        for (Address address : userDetails.getAddresses()) {
            address.setId(1L);
            address.setStreet("FC road");
            address.setCity("Pune");
            address.setState("Maharashtra");
            address.setCountry("India");
            address.setZipCode("415409");
            when(sequenceGeneratorService.generateSequence(Address.SEQUENCE_NAME)).thenReturn(1L);
            when(addressRepository.save(any(Address.class))).thenReturn(address);
            Address savedAddress = addressRepository.save(address);
            assertNotNull(savedAddress);

        }

        userDetails.setAddresses(addresses);
        when(userRepository.save(any(User.class))).thenReturn(userDetails);
        ApiResponse response = userService.addUser(userDto);
        assertNotNull(response);
        assertEquals(200, response.getHttpStatusCode());
    }


    @Test
    void testGetUserDetail() {
        long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("shrikantkhurd@gmail.com");
        mockUser.setPassword("123");
        mockUser.setFirstName("Shrikant");
        mockUser.setLastName("Khurd");
        mockUser.setContactNumber(987456321);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        UserDto userDto = userService.getUserDetail(userId);
        assertEquals(mockUser.getEmail(), userDto.getEmail());
    }





    //Address APIs test cases
   /* @Test
    void testAddAddress() {
        AddressDto addressDto = new AddressDto();
        addressDto.setId(1L);
        addressDto.setStreet("FC road");
        addressDto.setCity("Pune");
        addressDto.setState("Maharashtra");
        addressDto.setCountry("India");
        addressDto.setZipCode("415409");

        Address address= AddressMapper.mapToAddress(addressDto);

        when(sequenceGeneratorService.generateSequence(Address.SEQUENCE_NAME)).thenReturn(1L);
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDto addedAddress = userService.addAddress(addressDto);

        assertNotNull(addedAddress);
        assertEquals(address.getId(), addedAddress.getId());
        assertEquals(addressDto.getStreet(), addedAddress.getStreet());
        assertEquals(addressDto.getCity(), addedAddress.getCity());
        assertEquals(addressDto.getState(), addedAddress.getState());
        assertEquals(addressDto.getCountry(), addedAddress.getCountry());
        assertEquals(addressDto.getZipCode(), addedAddress.getZipCode());
    }*/


//    @Test
//    void testGetAllAddresses() {
//        List<Address> addresses = new ArrayList<>();
//        addresses.add(new Address(1L,"Street 123", "City", "State", "Country", "12345",false,true));
//        addresses.add(new Address(2L,"Another Street", "Another City", "Another State", "Another Country", "54321",true,false));
//
//        when(addressRepository.findAll()).thenReturn(addresses);
//
//        List<Address> allAddresses = userService.getAllAddresses();
//
//        assertNotNull(allAddresses);
//        assertEquals(addresses.size(), allAddresses.size());
//        assertEquals(addresses, allAddresses);
//    }

    @Test
    void testAddUserAddress() {
        AddressDto addressDto = new AddressDto(1L, "Street 123", "City", "State", "Country", "12345", true, false);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("shrikantkhurd@gmail.com");
        mockUser.setPassword("123");
        mockUser.setFirstName("Shrikant");
        mockUser.setLastName("Khurd");
        mockUser.setContactNumber(987456321);
        mockUser.setAccountStatus(true);
        List<Address> addresses = new ArrayList<>();
        mockUser.setAddresses(addresses);

        Address address = AddressMapper.mapToAddress(addressDto);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(sequenceGeneratorService.generateSequence(Address.SEQUENCE_NAME)).thenReturn(1L);
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        ApiResponse response = userService.addUserAddress(addressDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(1, mockUser.getAddresses().size());
    }


    /* @Test
     void testGetAddressesByUser() {
         long userId = 1;
         User user = new User();
         List<Address> addresses = new ArrayList<>();
         addresses.add(new Address(1L,"Street 123", "City", "State", "Country", "12345",true,false));
         user.setAddresses(addresses);

         when(userRepository.findById(userId)).thenReturn(Optional.of(user));

         List<AddressDto> addressList = userService.getAddressesByUser(userId);

         assertNotNull(addressList);
         assertEquals(addresses.size(), addressList.size());
     }*/
    @Test
    void testGetAddressesByUserId() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("shrikantkhurd@gmail.com");
        mockUser.setPassword("123");
        mockUser.setFirstName("Shrikant");
        mockUser.setLastName("Khurd");
        mockUser.setContactNumber(987456321);

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(1L, "Street 123", "City", "State", "Country", "12345", false, true));
        addresses.add(new Address(2L, "Another Street", "Another City", "Another State", "Another Country", "54321", true, false));
        mockUser.setAddresses(addresses);

        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        List<AddressDto> addressList = new ArrayList<>();
        for (Address address : addresses) {
            AddressDto addressDto = AddressMapper.mapToAddressDto(address);
            addressList.add(addressDto);
        }

        ApiResponse response = userService.getAddressesByUserId(mockUser.getId());
        System.out.println(response.getData());
        System.out.println(mockUser.getAddresses());
        assertNotNull(response);
//        assertEquals(addressList, response.getData());

        List<AddressDto> responseData = (List<AddressDto>) response.getData();

        assertNotNull(response);
//        assertEquals(addressList.size(), responseData.size());

        // Compare content of lists
        for (int i = 0; i < addressList.size(); i++) {
            assertEquals(addressList.get(i), responseData.get(i));
        }
    }


    @Test
    void testGetAddressDetailByAddressId() {
        User user = new User();
        user.setId(1L);
        Address address = new Address();
        address.setId(1L);
        address.setStreet("FC road");
        address.setCity("Pune");
        address.setState("Maharashtra");
        address.setCountry("India");
        address.setZipCode("415409");
        user.setAddresses(Collections.singletonList(address));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        ApiResponse response = userService.getAddressDetailByAddressId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getData());

//        String expectedMessage = messageSource.getMessage("api.response.user.address.detail", null, Locale.ENGLISH);
//        System.out.println("Expected Message: " + expectedMessage);
//        assertEquals(expectedMessage, response.getMessage());
    }

    @Test
    void testGetAddressDetailByAddressId_ThrowsResourceNotFoundException() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.getAddressDetailByAddressId(1L));

        assertNotNull(exception);
//        String expectedMessage = messageSource.getMessage("api.error.address.not.found", null, Locale.ENGLISH);
//        System.out.println("Expected Message: " + expectedMessage);
//        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUpdateUserAddress_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.updateUserAddress(new AddressDto(), 1L));

        assertNotNull(exception);
//        String expectedMessage = messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH);
//        System.out.println("Expected Message: " + expectedMessage);
//        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUpdateUserAddress_ThrowsAddressNotFound() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.updateUserAddress(new AddressDto(), 1L));

        assertNotNull(exception);
//        String expectedMessage = messageSource.getMessage("api.error.address.not.found", null, Locale.ENGLISH);
//        System.out.println("Expected Message: " + expectedMessage);
//        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUpdateUserAddressDetails() {
        User user = new User();
        user.setId(1L);
        Address address = new Address();
        address.setId(1L);
        address.setStreet("FC road");
        address.setCity("Pune");
        address.setState("Maharashtra");
        address.setCountry("India");
        address.setZipCode("415409");
//        user.setAddresses(Collections.singletonList(address));
        user.setAddresses(List.of(address));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        AddressDto updatedAddressDto = new AddressDto();
        updatedAddressDto.setStreet("Cybage Tower");
        updatedAddressDto.setCity("Mumbai");
        updatedAddressDto.setState("Maharashtra");
        updatedAddressDto.setCountry("India");
        updatedAddressDto.setZipCode("415428");

        Address newAddress = AddressMapper.mapToAddress(updatedAddressDto);
        when(addressRepository.save(any(Address.class))).thenReturn(newAddress);

        ApiResponse response = userService.updateUserAddress(updatedAddressDto, 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(newAddress.getStreet(), address.getStreet());
        assertEquals(newAddress.getCity(), address.getCity());
        assertEquals(newAddress.getZipCode(), address.getZipCode());

//        String expectedMessage = messageSource.getMessage("api.response.user.update.address", null, Locale.ENGLISH);
//        System.out.println("Expected Message: " + expectedMessage);
//        assertEquals(expectedMessage, response.getMessage());
    }

    @Test
    void testSetAsDefaultUserAddress() {
        User user = new User();
        user.setId(1L);
        Address address1 = new Address();
        address1.setId(1L);
        address1.setStreet("Cybage Tower");
        address1.setDefaultAddress(true);
        Address address2 = new Address();
        address2.setId(2L);
        address2.setStreet("FC Road");
        address2.setDefaultAddress(false);
        Address address3 = new Address();
        address3.setId(3L);
        address3.setStreet("EON IT Park");
        address3.setDefaultAddress(false);
        user.setAddresses(Arrays.asList(address1, address2, address3));

        assertTrue(address1.isDefaultAddress());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address2));

        ApiResponse response = userService.setAsDefaultUserAddress(3L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertFalse(address1.isDefaultAddress());
        assertFalse(address2.isDefaultAddress());
        assertTrue(address3.isDefaultAddress());

//        String expectedMessage = messageSource.getMessage("api.response.user.set.default.address", null, Locale.ENGLISH);
//        System.out.println("Expected Message: " + expectedMessage);
//        assertEquals(expectedMessage, response.getMessage());
    }

    @Test
    void testDeleteAddress_SetsDeletedAddressStatus() {
        // Arrange
        User user = new User();
        user.setId(1L);
        Address address1 = new Address();
        address1.setId(1L);
        address1.setDeletedAddressStatus(false);
        Address address2 = new Address();
        address2.setId(2L);
        address2.setDeletedAddressStatus(false);
        user.setAddresses(Arrays.asList(address1, address2));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address2));

//        assertTrue(address2.isDeletedAddressStatus());

        ApiResponse response = userService.deleteAddress(2L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertFalse(address1.isDeletedAddressStatus());
        assertTrue(address2.isDeletedAddressStatus());

//        String expectedMessage = messageSource.getMessage("api.response.user.delete.address", null, Locale.ENGLISH);
//        System.out.println("Expected Message: " + expectedMessage);
//        System.out.println("Message: " + response.getMessage());
//        assertEquals(expectedMessage, response.getMessage());
    }

}