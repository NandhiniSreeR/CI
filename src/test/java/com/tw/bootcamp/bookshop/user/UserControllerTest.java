package com.tw.bootcamp.bookshop.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.error.EmailDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

import static com.tw.bootcamp.bookshop.user.UserTestBuilder.buildCreateUserRequest;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Test
    void shouldCreateUserWhenCredentialsAreValid() throws Exception {
        String email = "testemail@test.com";
        CreateUserRequest userCredentials = buildCreateUserRequest();
        User user = new UserTestBuilder().withId(1L).withEmail(email).build();
        when(userService.create(userCredentials)).thenReturn(user);
        UserResponse userResponse = UserResponse.builder().id(user.getId().toString()).email(email).build();

        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userCredentials))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(userResponse)));

        verify(userService, times(1)).create(userCredentials);
    }

    @Test
    void shouldRespondWithErrorMessageWhenCreateUserFails() throws Exception {
        CreateUserRequest userCredentials = buildCreateUserRequest();
        when(userService.create(userCredentials)).thenThrow(new InvalidEmailException());

        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userCredentials))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("User with same email already created"));
    }

    @Test
    void shouldRespondWithErrorMessageWhenCreateUserValidationFails() throws Exception {
        CreateUserRequest userCredentials = new CreateUserRequest("", "foobar");
        Set<ConstraintViolation<User>> violations = validator.validate(User.create(userCredentials));
        when(userService.create(userCredentials)).thenThrow(new ConstraintViolationException(violations));

        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userCredentials))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.email").value("Email is mandatory"));
    }

    @Test
    void shouldRespondWithErrorMessageWhenGivenInvalidEmail() throws Exception {
        CreateUserRequest userCredentials = CreateUserRequest.builder()
                .email("nanotestgoogl.com")
                .password("password")
                .build();
        when(userService.create(userCredentials)).thenThrow(new InvalidEmailPatternException());

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userCredentials))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Email should follow the pattern abc@xyz.com"));
    }

    @Test
    void shouldRespondWithErrorMessageWhenGivenEmptyPassword() throws Exception {
        CreateUserRequest userCredentials = CreateUserRequest.builder()
                .email("nanotest@googl.com")
                .password("")
                .build();
        when(userService.create(userCredentials)).thenThrow(new PasswordEmptyException());

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userCredentials))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Password can't be empty"));
    }

    @Test
    void shouldRespondWithErrorMessageWhenGivenWeakPassword() throws Exception {
        CreateUserRequest userCredentials = CreateUserRequest.builder()
                .email("nanotest@googl.com")
                .password("password")
                .build();
        when(userService.create(userCredentials)).thenThrow(new InvalidPasswordPatternException());

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userCredentials))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Password should be atleast 8 characters with one one special" +
                                " character and one capital alphabet."));
    }

    @Test
    void shouldNotUpdateUserRoleToAdminWhenUserIsNotPresent() throws Exception {
        User user = new User("john@books.com", Role.ADMIN);
        when(userService.updateRole(any())).thenThrow(new EmailDoesNotExistException());

        mockMvc.perform(put("/admin/role")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                .with(user("admin@bookshopify.com").password("admin").roles(Role.ADMIN.name())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldUpdateUserRoleToAdminWhenAdminUpdates() throws Exception {
        User user = new User("john@books.com", Role.ADMIN);
        mockMvc.perform(put("/admin/role")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin@bookshopify.com").password("admin").roles(Role.ADMIN.name())))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotUpdateUserRoleToAdminWhenUserUpdates() throws Exception {
        User user = new User("john@books.com", Role.ADMIN);
        mockMvc.perform(put("/admin/role")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("user@bookshopify.com").password("user").roles(Role.USER.name())))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldFailWhenInvalidEmailAddressIsPassed() throws Exception {
        User user = new User("john", Role.ADMIN);
        when(userService.updateRole(any())).thenThrow(new EmailDoesNotExistException());

        mockMvc.perform(put("/admin/role")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin@bookshopify.com").password("admin").roles(Role.ADMIN.name())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldFailWhenInvalidRolesIsPassed() throws Exception {
        mockMvc.perform(put("/admin/role")
                        .content(objectMapper.writeValueAsString("{ 'email':'test@test.com', 'role':'DUMMY'} "))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin@bookshopify.com").password("admin").roles(Role.ADMIN.name())))
                .andExpect(status().isBadRequest());
    }
}