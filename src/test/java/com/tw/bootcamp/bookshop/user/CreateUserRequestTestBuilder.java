package com.tw.bootcamp.bookshop.user;

public class CreateUserRequestTestBuilder {
    private CreateUserRequest.CreateUserRequestBuilder requestBuilder;

    public CreateUserRequestTestBuilder() {
        requestBuilder = CreateUserRequest.builder()
                .email("testemail@test.com")
                .password("Foo*bart");
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

    public CreateUserRequestTestBuilder withEmptyUsername() {
        requestBuilder.email("@tw.com");
        return this;
    }

    public CreateUserRequestTestBuilder withEmptyTLD() {
        requestBuilder.email("test@.com");
        return this;
    }

    public CreateUserRequestTestBuilder withEmptyExtension() {
        requestBuilder.email("test@tw.");
        return this;
    }

    public CreateUserRequestTestBuilder withEmptyExtensionAndTLD() {
        requestBuilder.email("test@.");
        return this;
    }

    public CreateUserRequestTestBuilder withPasswordAllSmall() {
        requestBuilder.password("passwor@d");
        return this;
    }

    public CreateUserRequestTestBuilder withPasswordSize6() {
        requestBuilder.password("Pwor@d");
        return this;
    }

    public CreateUserRequestTestBuilder withNoSpecialCharacterPassword() {
        requestBuilder.password("Password");
        return this;
    }
}
