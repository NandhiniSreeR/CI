package com.tw.bootcamp.bookshop.user.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
@WithMockUser
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAddressWhenValid() throws Exception {
        CreateAddressRequest createRequest = createAddress();
        Address address = new AddressTestBuilder().build();
        when(addressService.create(eq(createRequest), any(User.class))).thenReturn(address);
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(new UserTestBuilder().build()));

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(createRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lineNoOne").value("4 Privet Drive"))
                .andExpect(jsonPath("$.lineNoTwo").value("Little Whinging"))
                .andExpect(jsonPath("$.city").value("Godstone"))
                .andExpect(jsonPath("$.pinCode").value("A22 001"))
                .andExpect(jsonPath("$.country").value("Surrey"));

        verify(addressService, times(1)).create(eq(createRequest), any(User.class));
    }

    @Test
    void shouldNotCreateAddressWhenInValid() throws Exception {
        CreateAddressRequest createRequest = CreateAddressRequest.builder().city(null).build();
        when(addressService.create(any(), any())).thenThrow(new ConstraintViolationException(new HashSet<>()));
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(new UserTestBuilder().build()));

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(createRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verify(addressService, times(1)).create(any(), any());
    }

    @Test
    void shouldReturnAddressesForUserWhenEmailExists() throws Exception {
        String userEmail = "testemail@test.com";
        User user = new UserTestBuilder().build();
        when(userService.findByEmail(userEmail)).thenReturn(Optional.of(user));

        Address address = new AddressTestBuilder().build();
        List<Address> addresses = Collections.singletonList(address);
        when(addressService.loadAddressFromUserName(user)).thenReturn(addresses);

        mockMvc.perform(get("/addresses/" + userEmail))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].city").value(address.getCity()));
    }

    @Test
    void shouldFailToReturnAddressesForUserWhenEmailDoesNotExist() throws Exception{
        String invalidEmail = "test@test.com";
        when(userService.findByEmail(invalidEmail)).thenReturn(Optional.empty());
        List<Address> addresses = Collections.singletonList(new AddressTestBuilder().build());
        when(addressService.loadAddressFromUserName(any(User.class))).thenReturn(addresses);

        mockMvc.perform(get("/addresses/" + invalidEmail))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("User email does not exist"));
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
}