package com.tw.bootcamp.bookshop.user;

public class CreateUserRequestTestBuilder {
    private CreateUserRequest.CreateUserRequestBuilder requestBuilder;

    public CreateUserRequestTestBuilder() {
        requestBuilder = CreateUserRequest.builder()
                .email("testemail@test.com")
                .password("foobar");
    }

    CreateUserRequest build() {
        return requestBuilder.build();
    }

    public CreateUserRequestTestBuilder withEmptyEmail() {
        requestBuilder.email("");
        return this;
    }

    public CreateUserRequestTestBuilder withEmptyPassword() {
        requestBuilder.password("");
        return this;
    }

    public CreateUserRequestTestBuilder withInvalidEmail() {
        requestBuilder.email("nanotestgoogle.com");
        return this;
    }
}
