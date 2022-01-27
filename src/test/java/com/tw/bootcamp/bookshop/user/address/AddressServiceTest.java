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
        // ARRANGE
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress();
        Address address = addressService.create(createRequest, user);
        // ACT
        List<Address> userAddresses =  addressService.loadAddressFromUserName(user);
        // ASSERT
        assertEquals(1, userAddresses.size());
        assertEquals(address.getId(), userAddresses.get(0).getId());
    }

    @Test
    void shouldReturnErrorResponseWhenInvalidCityInInput() {
        // ARRANGE
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = addressWithInvalidCity();
        Address address = addressService.create(createRequest, user);
        // ACT
        AddressErrorResponse errorResponses =  addressService.validate(address);
        // ASSERT
        assertEquals(1, errorResponses.getErrors().size());
    }

    private CreateAddressRequest invalidAddress() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city(null)
                .pinCode("A22 001")
                .country("Surrey")
                .build();
    }

    private CreateAddressRequest createAddress() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("Godstone")
                .pinCode("A22 001")
                .country("Surrey")
                .build();
    }

    private CreateAddressRequest addressWithInvalidCity() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("123")
                .pinCode("A22 001")
                .country("Surrey")
                .build();
    }
}