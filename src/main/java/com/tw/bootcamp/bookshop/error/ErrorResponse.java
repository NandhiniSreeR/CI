package com.tw.bootcamp.bookshop.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    @Schema(example = "400", description = "Status of the Request")
    private final HttpStatus status;
    @Schema(example = "Bad Request", description = "details of the request")
    private final String message;
    @Schema(description = "errors of the request")
    private final Map<String, String > errors;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.errors = new HashMap<>();
    }
}
