package com.smartshopper.authservice.service;

import com.smartshopper.authservice.exception.ResourceNotFoundException;
import com.smartshopper.authservice.repository.AddressRepository;
import com.smartshopper.authservice.repository.UserRepository;
import com.smartshopper.authservice.repository.UserRoleRepository;
import com.smartshopper.authservice.utils.SequenceGenaratorService;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageResourceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final SequenceGenaratorService sequenceGeneratorService = mock(SequenceGenaratorService.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    private final UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
    private final AddressRepository addressRepository = mock(AddressRepository.class);
    private final MessageSource messageSource = mock(MessageSource.class);

    //    @Autowired
//    private MessageSource messageSource;
    private final UserService userService = new UserService(userRepository, userRoleRepository, addressRepository,
            sequenceGeneratorService, messageSource, bCryptPasswordEncoder);



    @Test
     void testGetMessage() {
        String message = messageSource.getMessage("response.message.key", null, Locale.ENGLISH);
//        assertEquals("Hello, World!", message);

        String expectedMessage = "Response Message";
        when(messageSource.getMessage(eq("response.message.key"), any(Object[].class), eq(Locale.US)))
                .thenReturn(expectedMessage);

//        String msg=  userService.testGetErrorMessage();

        String actualMessage = messageSource.getMessage("response.message.key", null, Locale.US);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }
    @Test
    void getResponseMessage() {
        String msg= messageSource.getMessage("response.message.key", null, Locale.ENGLISH);
//        System.out.println(msg);
        userService.testGetErrorMessage();
    }



    @Test
    void testGetUserDetail_UserDoesNotExistException() {
        Long currentUserId = 1L;
        String errorMessage = messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH);
        System.out.println("expected message: " + errorMessage);

        when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> userService.getUserDetail(currentUserId));
        String expectedMessage = messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH);
        System.out.println("Exception message: " + exception.getMessage());
        System.out.println("expected message: " + expectedMessage);

        String message = "Requested User Not Found In Registry. Please check and retry.";
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testGetUserDetail_UserDoesNotExist_1() {
        Long currentUserId = 1L;

        // Mock the behavior of messageSource.getMessage
//        when(messageSource.getMessage(eq("api.error.user.not.found"), any(), eq(Locale.ENGLISH)))
//                .thenReturn("User not found");
//
//        // Call the method under test and assert the exception
//        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.getUserDetail(currentUserId));
//
//        // Verify that the correct message was returned
//        String expectedMessage = "User not found";
//        assertEquals(expectedMessage, exception.getMessage());
//
        String expectedMessage1 = messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH);
        System.out.println("Expected Message: " + expectedMessage1);


//        ResourceNotFoundException exception1 = assertThrows(ResourceNotFoundException.class, () -> userService.getUserDetail(currentUserId));
//        System.out.println("Exception Message: " + exception1.getMessage());
    }



    @Test
    void testGetAllUsers() {

        File file = new File("src/test/resources/messages/api_error_messages.properties");
        if (file.exists()) {
            System.out.println("File found at: " + file.getAbsolutePath());
        } else {
            System.out.println("File not found.");
        }
        String classpath = System.getProperty("java.class.path");
        System.out.println("Classpath: " + classpath);

        URL s = getClass().getClassLoader().getResource("/messages/api_error_messages.properties");
        System.out.println("sdsfd: " + s);

        URL resourceUrl = getClass().getClassLoader().getResource("/messages/api_error_messages.properties");
        System.out.println("resource path :" + resourceUrl);
        if (resourceUrl != null) {
            System.out.println("Resource found at: " + resourceUrl.getPath());
        } else {
            System.out.println("Resource not found.");
        }

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("api_error_messages.properties")) {
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } else {
                System.out.println("Resource not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
