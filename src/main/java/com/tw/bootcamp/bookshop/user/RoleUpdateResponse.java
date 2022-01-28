package com.tw.bootcamp.bookshop.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoleUpdateResponse {

    @Schema(example = "user@example.com", description = "email of user")
    private final String email;
    @Schema(example = "ADMIN", description = "role of user")
    private final Role role;
    @Schema(example = "Role Updated Successfully", description = "Status of the role update")
    private final String message;


}
