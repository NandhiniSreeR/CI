package com.tw.bootcamp.bookshop.user;

import com.tw.bootcamp.bookshop.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    @Operation(summary = "Create user", description = "Creates user with valid credentials. Password and Email cannot be empty." +
            "Email must follow the standard email pattern. eg. abc@xyz.com", tags = {"User Service"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "User created",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class))}),
                    @ApiResponse(responseCode = "400", content = @Content),
                    @ApiResponse(responseCode = "422", content = @Content)
            }
    )
    ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest userRequest) throws InvalidEmailException, InvalidEmailPatternException, PasswordEmptyException, InvalidPasswordPatternException {
        User user = userService.create(userRequest);
        return new ResponseEntity<>(new UserResponse(user), HttpStatus.CREATED);
    }

    @PutMapping("/admin/role")
    @Operation(summary = "Update User Role", description = "Admin user can update the role for any user", tags = {"User Service"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Role Updated Successfully",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleUpdateResponse.class))}),
                    @ApiResponse(responseCode = "400", description = "Request body has invalid params",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))}),
                    @ApiResponse(responseCode = "422", description = "When a unprocessable entity is passed in the body",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))})
            }
    )
    ResponseEntity updateRole(@RequestBody User user) {
        userService.updateRole(user);
        return ResponseEntity.status(HttpStatus.OK).body(new RoleUpdateResponse(user.getEmail(), user.getRole(), "Role Updated Successfully"));
    }

}
