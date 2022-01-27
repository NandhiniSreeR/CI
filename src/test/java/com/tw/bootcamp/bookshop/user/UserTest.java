package com.tw.bootcamp.bookshop.user;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.tw.bootcamp.bookshop.user.User.PASSWORD_ENCODER;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void shouldBeEmailMandatory() {
        CreateUserRequest userRequest = new CreateUserRequestTestBuilder().withEmptyEmail().build();
        User user = User.create(userRequest);

        Set<ConstraintViolation<User>> constraintViolations = constraintsValidator().validate(user);

        assertFalse(constraintViolations.isEmpty());
        ConstraintViolation<User> next = constraintViolations.iterator().next();
        assertEquals("Email is mandatory", next.getMessage());
    }

    @Test
    void shouldBePasswordMandatory() {
        CreateUserRequest userRequest = new CreateUserRequestTestBuilder().withEmptyPassword().build();
        User user = User.create(userRequest);

        Set<ConstraintViolation<User>> constraintViolations = constraintsValidator().validate(user);

        assertFalse(constraintViolations.isEmpty());
        assertEquals("Password is mandatory", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void shouldCreateUserEncryptPassword() {
        CreateUserRequest userRequest = new CreateUserRequestTestBuilder().build();
        User user = User.create(userRequest);

        assertTrue(PASSWORD_ENCODER.matches("Foo*bart", user.getPassword()));
    }

    private Validator constraintsValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        return validatorFactory.getValidator();
    }
}