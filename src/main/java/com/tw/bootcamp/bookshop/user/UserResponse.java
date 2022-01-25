package com.tw.bootcamp.bookshop.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserResponse {
    @Schema(example = "1", description = "Unique Identifier of user")
    private final String id;
    @Schema(example = "user@example.com", description = "email of user")
    private final String email;

    public UserResponse(User user) {
        this.id = user.getId().toString();
        this.email = user.getEmail();
    }
}
