package com.smartshopper.authservice.service;

import com.smartshopper.authservice.dto.AddressDto;
import com.smartshopper.authservice.dto.ApiResponse;
import com.smartshopper.authservice.dto.UserDto;
import com.smartshopper.authservice.exception.EmailAlreadyExistException;
import com.smartshopper.authservice.exception.ResourceNotFoundException;
import com.smartshopper.authservice.mapper.AddressMapper;
import com.smartshopper.authservice.mapper.UserMapper;
import com.smartshopper.authservice.model.Address;
import com.smartshopper.authservice.model.User;
import com.smartshopper.authservice.model.UserRole;
import com.smartshopper.authservice.repository.AddressRepository;
import com.smartshopper.authservice.repository.UserRepository;
import com.smartshopper.authservice.repository.UserRoleRepository;
import com.smartshopper.authservice.security.JwtFilter;
import com.smartshopper.authservice.utils.ConstantMethods;
import com.smartshopper.authservice.utils.SequenceGenaratorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    //	@Autowired
    private final UserRepository userRepository;
    //	@Autowired
    private final UserRoleRepository userRoleRepository;
    //	@Autowired
    private final AddressRepository addressRepository;
    //	@Autowired
    private final SequenceGenaratorService sequenceGeneratorService;
    //	@Autowired
    private final MessageSource messageSource;
    //	@Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

//	@Autowired
//	public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository,
//			AddressRepository addressRepository, SequenceGenaratorService sequenceGeneratorService,
//			MessageSource messageSource, BCryptPasswordEncoder bCryptPasswordEncoder,AuthJwtService authService) {
//		super();
//		this.userRepository = userRepository;
//		this.userRoleRepository = userRoleRepository;
//		this.addressRepository = addressRepository;
//		this.sequenceGeneratorService = sequenceGeneratorService;
//		this.messageSource = messageSource;
//		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//		this.authService=authService;
//	}

    //	public User getUserByEmail(String email) {
//		return userRepository.findByEmail(email);
//	}
    public String testGetErrorMessage() {
        String message = messageSource.getMessage("response.message.key", null, Locale.US);
        System.out.println(message);
        return message;
    }

    public ApiResponse addUser(UserDto userDto) {
        User byEmail = userRepository.findByEmail(userDto.getEmail());
        if (byEmail == null) {
            User userDetails = UserMapper.mapToUser(userDto);
            userDetails.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
            userDetails.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            userDetails.setCreatedAt(LocalDateTime.now());
            userDetails.setAccountStatus(true);

            Set<UserRole> role = userRoleRepository.findByRoleId((long) 2);
            userDetails.setRoles(role);

            List<Address> addressList = new ArrayList<>();
            for (Address address : userDto.getAddresses()) {
                address.setId(sequenceGeneratorService.generateSequence(Address.SEQUENCE_NAME));
                address.setDefaultAddress(true);
                Address savedAddress = addressRepository.save(address);
                addressList.add(savedAddress);
            }
            userDetails.setAddresses(addressList);

            User savedUser = userRepository.save(userDetails);
            UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);
            return ConstantMethods.successResponse(savedUserDto,
                    messageSource.getMessage("api.response.user.registered.successfully", null, Locale.ENGLISH));
        } else {
            throw new EmailAlreadyExistException(
                    messageSource.getMessage("api.error.user.already.registered", null, Locale.ENGLISH));
        }
    }

    public UserDto getUserDetail() {
        User user = userRepository.findById(JwtFilter.CURRENT_USER_ID).orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH)));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto getUserDetail(Long userId) {
        log.info("get user details method called");
        log.info(messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH)));
        return UserMapper.mapToUserDto(user);
    }

    public ApiResponse getById(Long userId) {
        log.info("get user details method called");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH)));

        UserDto userDto = UserMapper.mapToUserDto(user);
        return ConstantMethods.successResponse(userDto,
                messageSource.getMessage("api.response.user.get.successfully", null, Locale.ENGLISH));
    }

    public ApiResponse getAllUsers() {
        log.info("get all users method called");
        List<User> allUsers = userRepository.findAll();
        return ConstantMethods.successResponse(allUsers,
                messageSource.getMessage("api.response.user.get.all.users", null, Locale.ENGLISH));
    }

    public void deleteUser(long userId) {
        log.info("delete user method called");
        userRepository.deleteById(userId);
    }

    // ADDRESS APIs
/*	public AddressDto addAddress(AddressDto address) {
        Address addAddress = AddressMapper.mapToAddress(address);
        addAddress.setId(sequenceGeneratorService.generateSequence(Address.SEQUENCE_NAME));
        Address savedAddress = addressRepository.save(addAddress);
        return AddressMapper.mapToAddressDto(savedAddress);
    }*/

    public List<Address> getAllAddresses() {
        log.info("get all users method called");
        return addressRepository.findAll();
    }

    public ApiResponse addUserAddress(AddressDto addressDto) {
        User user = userRepository.findById(JwtFilter.CURRENT_USER_ID).orElse(null);
        if (user != null && user.isAccountStatus()) {
            Address addAddress = AddressMapper.mapToAddress(addressDto);
            addAddress.setId(sequenceGeneratorService.generateSequence(Address.SEQUENCE_NAME));
            addAddress.setDefaultAddress(false);
            addAddress.setDeletedAddressStatus(false);
            Address savedAddress = addressRepository.save(addAddress);
            user.getAddresses().add(savedAddress);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            return ConstantMethods.successResponse(null,
                    messageSource.getMessage("api.response.user.add.address", null, Locale.ENGLISH));
        }
        return null;
    }

    public ApiResponse getAddressesByUserId() {
        User user = userRepository.findById(JwtFilter.CURRENT_USER_ID).orElse(null);
        if (user != null) {
            List<AddressDto> addressList = new ArrayList<>();
            for (Address address : user.getAddresses()) {
                if (!address.isDeletedAddressStatus()) {
                    AddressDto addressDto = AddressMapper.mapToAddressDto(address);
                    addressList.add(addressDto);
                }
            }
            return ConstantMethods.successResponse(addressList,
                    messageSource.getMessage("api.response.user.all.address", null, Locale.ENGLISH));
        }
        return null;
    }

    public ApiResponse getAddressesByUserId(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            List<AddressDto> addressList = new ArrayList<>();
            for (Address address : user.getAddresses()) {
                if (!address.isDeletedAddressStatus()) {
                    AddressDto addressDto = AddressMapper.mapToAddressDto(address);
                    addressList.add(addressDto);
                }
            }
            return ConstantMethods.successResponse(addressList,
                    messageSource.getMessage("api.response.user.all.address", null, Locale.ENGLISH) );
        }
        return null;
    }

    /*public List<AddressDto> getAddressesByUser(long userId) {
        User user = userRepository.findById(userId).orElse(null);
        List<AddressDto> addressList = new ArrayList<>();
        if (user != null) {
            for (Address address : user.getAddresses()) {
                AddressDto addressDto = AddressMapper.mapToAddressDto(address);
                addressList.add(addressDto);
            }
        }
        return addressList;
    }*/

    public ApiResponse getAddressDetailByAddressId(long addressId) {
        User user = userRepository.findById(JwtFilter.CURRENT_USER_ID).orElse(null);
        if (user != null) {
            AddressDto addressDto = new AddressDto();
            boolean addressFound = false;
            for (Address address : user.getAddresses()) {
                if (address.getId() == addressId) {
                    Address addressDetails = addressRepository.findById(addressId).orElse(null);
                    if (addressDetails != null) {
                        addressDto = AddressMapper.mapToAddressDto(addressDetails);
                        addressFound = true;
                        break;
                    }
                }
            }
            if (!addressFound) {
                throw new ResourceNotFoundException(
                        messageSource.getMessage("api.error.address.not.found", null, Locale.ENGLISH));
            }
            return ConstantMethods.successResponse(addressDto,
                    messageSource.getMessage("api.response.user.address.detail", null, Locale.ENGLISH));
        }
        return null;
    }

    public AddressDto getAddressByAddressId(long addressId) {
        User user = userRepository.findById(JwtFilter.CURRENT_USER_ID).orElse(null);
        if (user != null) {
            AddressDto addressDto = new AddressDto();
            boolean addressFound = false;
            for (Address address : user.getAddresses()) {
                if (address.getId() == addressId) {
                    Address addressDetails = addressRepository.findById(addressId).orElse(null);
                    if (addressDetails != null) {
                        addressDto = AddressMapper.mapToAddressDto(addressDetails);
                        addressFound = true;
                        break;
                    }
                }
            }
            if (!addressFound) {
                throw new ResourceNotFoundException(
                        messageSource.getMessage("api.error.address.not.found", null, Locale.ENGLISH));
            }
            return addressDto;
        }
        return null;
    }

    public ApiResponse updateUserAddress(AddressDto updatedAddressDto, long addressId) {
        User user = userRepository.findById(JwtFilter.CURRENT_USER_ID).orElse(null);
        if (user != null) {
            boolean addressUpdated = false;
            for (Address address : user.getAddresses()) {
                if (address.getId() == addressId) {
                    address.setStreet(updatedAddressDto.getStreet());
                    address.setCity(updatedAddressDto.getCity());
                    address.setState(updatedAddressDto.getState());
                    address.setCountry(updatedAddressDto.getCountry());
                    address.setZipCode(updatedAddressDto.getZipCode());
                    address.setDeletedAddressStatus(false);
                    addressRepository.save(address);
                    addressUpdated = true;
                    break;
                }
            }
            if (!addressUpdated) {
                throw new ResourceNotFoundException(
                        messageSource.getMessage("api.error.address.not.found", null, Locale.ENGLISH));
            }
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            return ConstantMethods.successResponse(null,
                    messageSource.getMessage("api.response.user.update.address", null, Locale.ENGLISH));
        }
        throw new ResourceNotFoundException(messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH));
    }

    public ApiResponse setAsDefaultUserAddress(long addressId) {
        User user = userRepository.findById(JwtFilter.CURRENT_USER_ID).orElse(null);
        if (user != null) {
            boolean addressUpdated = false;
            for (Address address : user.getAddresses()) {
                address.setDefaultAddress(address.getId() == addressId);
                addressRepository.save(address);
                addressUpdated = true;
            }
            if (!addressUpdated) {
                throw new ResourceNotFoundException(
                        messageSource.getMessage("api.error.address.not.found", null, Locale.ENGLISH));
            }
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            return ConstantMethods.successResponse(null,
                    messageSource.getMessage("api.response.user.set.default.address", null, Locale.ENGLISH));
        }
        return null;
    }

    public ApiResponse deleteAddress(long addressId) {
        User user = userRepository.findById(JwtFilter.CURRENT_USER_ID).orElse(null);
        if (user != null) {
            boolean addressFound = false;
            for (Address address : user.getAddresses()) {
                if (address.getId() == addressId) {
                    address.setDeletedAddressStatus(true);
                    addressRepository.save(address);
                    addressFound = true;
                    break;
                }
            }
            if (!addressFound) {
                throw new ResourceNotFoundException(
                        messageSource.getMessage("api.error.address.not.found", null, Locale.ENGLISH));
            }
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            return ConstantMethods.successResponse(user,
                    messageSource.getMessage("api.response.user.delete.address", null, Locale.ENGLISH));
        }
        throw new ResourceNotFoundException(messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH));
    }
}
