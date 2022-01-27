package com.tw.bootcamp.bookshop.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
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
}
