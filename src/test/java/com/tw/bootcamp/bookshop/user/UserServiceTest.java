package com.tw.bootcamp.bookshop.user;

import com.tw.bootcamp.bookshop.error.EmailDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private UserService userService = new UserService();

    @Test
    void shouldCreateUserWithValidInputs() throws InvalidEmailException, InvalidEmailPatternException, PasswordEmptyException, InvalidPasswordPatternException {
        CreateUserRequest userCredentials = new CreateUserRequestTestBuilder().build();
        User user = new UserTestBuilder().withEmail(userCredentials.getEmail()).build();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(userCredentials);

        ArgumentCaptor<User> argCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(argCaptor.capture());
        assertEquals(userCredentials.getEmail(), argCaptor.getValue().getEmail());
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(Role.USER, createdUser.getRole());
    }

    @Test
    void shouldNotCreateUserWhenUserWithSameEmailAlreadyExists() {
        CreateUserRequest userRequest = new CreateUserRequestTestBuilder().build();
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(new User()));
        userRepository.save(User.create(userRequest));

        InvalidEmailException createUserException = assertThrows(InvalidEmailException.class,
                () -> userService.create(userRequest));
        assertEquals("User with same email already created", createUserException.getMessage());
    }

    @Test
    void shouldNotCreateUserWhenInputIsInvalid() {
        CreateUserRequest invalidRequest = new CreateUserRequestTestBuilder().withEmptyEmail().build();
        when(validator.validate(any(User.class))).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> userService.create(invalidRequest));
    }

    @Test
    void shouldReturnUserForGivenValidEmail() {
        String validEmailId = "testemail@test.com";
        User user = new UserTestBuilder().build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        assertEquals(user.getEmail(), userService.loadUserByUsername(validEmailId).getUsername());
    }

    @Test
    void shouldReturnUserNotFoundExceptionForGivenInValidEmail() {
        String invalidEmailId = "testemail@test";
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(invalidEmailId));
    }

    @Test
    void shouldNotCreateUserWhenEmailPatternDoesNotMatch() {
        CreateUserRequest invalidRequest = new CreateUserRequestTestBuilder().withInvalidEmail().build();
        assertThrows(InvalidEmailPatternException.class, () -> userService.create(invalidRequest));
    }

    @Test
    void shouldNotCreateUserWhenEmptyPasswordIsGiven() {
        CreateUserRequest invalidRequest = new CreateUserRequestTestBuilder().withEmptyPassword().build();
        assertThrows(PasswordEmptyException.class, () -> userService.create(invalidRequest));
    }

    @Test
    void shouldNotCreateUserWhenEmailDoesNotHaveUsername() {
        CreateUserRequest invalidRequest = new CreateUserRequestTestBuilder().withEmptyUsername().build();
        assertThrows(InvalidEmailPatternException.class, () -> userService.create(invalidRequest));
    }

    @Test
    void shouldNotCreateUserWhenEmailDoesNotHaveTLD() {
        CreateUserRequest invalidRequest = new CreateUserRequestTestBuilder().withEmptyTLD().build();
        assertThrows(InvalidEmailPatternException.class, () -> userService.create(invalidRequest));
    }

    @Test
    void shouldNotCreateUserWhenEmailTLDDoesNotContainDot() {
        CreateUserRequest invalidRequest = new CreateUserRequestTestBuilder().withNoDotEmail().build();
        assertThrows(InvalidEmailPatternException.class, () -> userService.create(invalidRequest));
    }

    @Test
    void shouldNotCreateUserWhenEmailDoesNotHaveExtension() {
        CreateUserRequest invalidRequest = new CreateUserRequestTestBuilder().withEmptyExtension().build();
        assertThrows(InvalidEmailPatternException.class, () -> userService.create(invalidRequest));
    }

    @Test
    void shouldNotCreateUserWhenEmailDoesNotHaveExtensionAndTLD() {
        CreateUserRequest invalidRequest = new CreateUserRequestTestBuilder().withEmptyExtensionAndTLD().build();
        assertThrows(InvalidEmailPatternException.class, () -> userService.create(invalidRequest));
    }

    @Test
    void shouldNotCreateUserWhenPasswordDoesNotHaveAtleastOneCapitalLetter() {
        CreateUserRequest invalidRequest = new CreateUserRequestTestBuilder().withPasswordAllSmall().build();
        assertThrows(InvalidPasswordPatternException.class, () -> userService.create(invalidRequest));
    }

    @Test
    void shouldNotCreateUserWhenPasswordSizeIsLessThan8() {
        CreateUserRequest invalidRequest = new CreateUserRequestTestBuilder().withPasswordSize6().build();
        assertThrows(InvalidPasswordPatternException.class, () -> userService.create(invalidRequest));
    }

    @Test
    void shouldNotCreateUserWhenPasswordDoesNotHaveSpecialCharacter() {
        CreateUserRequest invalidRequest = new CreateUserRequestTestBuilder().withNoSpecialCharacterPassword().build();
        assertThrows(InvalidPasswordPatternException.class, () -> userService.create(invalidRequest));
    }

    @Test
    void shouldReturnUpdatedUserWhenAdminChangesTheRole(){
        User nonAdminUser = new User("nonadmin@bookshopify.com", Role.USER);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(nonAdminUser));
        assertEquals(nonAdminUser.getRole(), userService.updateRole(nonAdminUser).getRole());
        assertEquals(nonAdminUser.getEmail(), userService.updateRole(nonAdminUser).getEmail());
    }
    @Test
    void shouldThrowEmailNotFoundExceptionWhenUserIsNotPresent(){
        User nonAdminUser = new User("nonadmin@bookshopify.com", Role.USER);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(EmailDoesNotExistException.class, () -> userService.updateRole(nonAdminUser));
    }


}