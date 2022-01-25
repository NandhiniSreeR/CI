package com.tw.bootcamp.bookshop.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class CreateUserRequest {
    @Schema(example = "user@example.com", description = "email of user")
    private final String email;
    @Schema(example = "password", description = "password of user")
    private final String password;
}
