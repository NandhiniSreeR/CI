package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.error.AddressErrorResponse;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserRepository;
import com.tw.bootcamp.bookshop.user.UserTestBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressService addressService;

    @AfterEach
    void tearDown() {
        addressRepository.deleteAll();
    }

    @Test
    void shouldCreateAddressWhenValid() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress();

        Address address = addressService.create(createRequest, user);

        assertNotNull(address);
        assertEquals("4 Privet Drive", address.getLineNoOne());
        assertEquals(user.getId(), address.getUser().getId());
    }

    @Test
    void shouldNotCreateAddressWhenInValid() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = invalidAddress();

        assertThrows(ConstraintViolationException.class, ()-> addressService.create(createRequest, user));
    }

    @Test
    void shouldNotCreateAddressWhenUserIsNotValid() {
        CreateAddressRequest createRequest = createAddress();
        assertThrows(DataIntegrityViolationException.class, ()-> addressService.create(createRequest, null));
    }

    @Test
    void shouldDisplayUserAddressesWhenGivenUserEmail() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress();
        Address address = addressService.create(createRequest, user);

        List<Address> userAddresses =  addressService.loadAddressForUser(user);

        assertEquals(1, userAddresses.size());
        assertEquals(address.getId(), userAddresses.get(0).getId());
    }

    @Test
    void shouldReturnErrorResponseWhenInvalidCityInInput() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = addressWithInvalidCity();

        AddressErrorResponse errorResponses =  addressService.validate(createRequest);

        assertTrue(errorResponses.hasAnyErrors());
    }

    @Test
    void shouldReturnTrueWhenAddressIsValid() {
        CreateAddressRequest createRequest = createAddress();

        AddressErrorResponse errorResponses =  addressService.validate(createRequest);

        assertFalse(errorResponses.hasAnyErrors());
    }

    @Test
    void shouldReturnErrorResponseWhenInvalidCountryInInput() {
        CreateAddressRequest createRequest = addressWithInvalidCountry();

        AddressErrorResponse errorResponses =  addressService.validate(createRequest);

        assertTrue(errorResponses.hasAnyErrors());
    }

    @Test
    void shouldReturnErrorResponseWhenCountryContainsAlphanumericInInput() {
        CreateAddressRequest createRequest = addressWithInvalidAlphanumericCountry();

        AddressErrorResponse errorResponses =  addressService.validate(createRequest);

        assertTrue(errorResponses.hasAnyErrors());
    }

    @Test
    void shouldReturnErrorResponseWhenInvalidCountryAndInvalidCityAndInvalidStateInInput() {
        CreateAddressRequest createRequest = addressWithInvalidCountryAndInvalidCityAndInvalidState();

        AddressErrorResponse errorResponses =  addressService.validate(createRequest);

        assertTrue(errorResponses.hasAnyErrors());
        assertEquals(3,errorResponses.countOfErrors());

    }

    @Test
    void shouldReturnErrorResponseWhenInvalidPincodeInInput() {
        CreateAddressRequest createRequest = addressWithInvalidPincode();

        AddressErrorResponse errorResponses =  addressService.validate(createRequest);

        assertTrue(errorResponses.hasAnyErrors());
    }

    @Test
    void shouldReturnErrorResponseWhenInvalidMobileNumberInInput() {
        CreateAddressRequest createRequest = addressWithInvalidMobileNumber();

        AddressErrorResponse errorResponses =  addressService.validate(createRequest);

        assertTrue(errorResponses.hasAnyErrors());
    }


    @Test
    void shouldCreateAddressWhenValidNameAndMobileNoAreInput() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddressWithNameAndMobileNo();

        Address address = addressService.create(createRequest, user);

        assertNotNull(address);
        assertEquals("4 Privet Drive", address.getLineNoOne());
        assertEquals(user.getId(), address.getUser().getId());
    }

    @Test
    void shouldMakeAddressAsDefaultWhenCreatingFirstTime() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddressWithNameAndMobileNo();

        Address address = addressService.create(createRequest, user);

        assertNotNull(address);
        assertTrue(address.isDefault());
        assertEquals("4 Privet Drive", address.getLineNoOne());
        assertEquals(user.getId(), address.getUser().getId());
    }

    @Test
    void shouldNotMakeAddressAsDefaultWhenCreatingTime() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddressWithNameAndMobileNo();

        Address address = addressService.create(createRequest, user);

        assertNotNull(address);
        assertTrue(address.isDefault());

        CreateAddressRequest secondCreateRequest = createAddressWithNameAndMobileNo();

        Address secondAddress = addressService.create(secondCreateRequest, user);

        assertNotNull(secondAddress);
        assertFalse(secondAddress.isDefault());

    }

    private CreateAddressRequest addressWithInvalidMobileNumber() {
        return CreateAddressRequest.builder()
                .fullName("Mr. X")
                .mobileNumber(98743210L)
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("Godstone")
                .pinCode("A22001")
                .country("Surrey")
                .build();
    }

    private CreateAddressRequest createAddressWithNameAndMobileNo() {
        return CreateAddressRequest.builder()
                .fullName("Mr. X")
                .mobileNumber(9876543210L)
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("Godstone")
                .pinCode("A22001")
                .country("Surrey")
                .build();
    }

    private CreateAddressRequest addressWithInvalidPincode() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("Bangalore")
                .pinCode("A22+001")
                .country("India")
                .state("Karnataka")
                .build();
    }

    private CreateAddressRequest addressWithInvalidCountryAndInvalidCityAndInvalidState() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("12345667")
                .pinCode("A22001")
                .country("123456")
                .state("123")
                .build();
    }

    private CreateAddressRequest addressWithInvalidCountry() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("Pune")
                .pinCode("A22001")
                .country("123456")
                .build();
    }

    private CreateAddressRequest addressWithInvalidAlphanumericCountry() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("Pune")
                .pinCode("A22001")
                .country("Bangalore1")
                .build();
    }

    private CreateAddressRequest invalidAddress() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city(null)
                .pinCode("A22001")
                .country("Surrey")
                .build();
    }

    private CreateAddressRequest createAddress() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("Godstone")
                .pinCode("A22001")
                .country("Surrey")
                .build();
    }

    private CreateAddressRequest addressWithInvalidCity() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("123")
                .pinCode("A22001")
                .country("Surrey")
                .build();
    }
}