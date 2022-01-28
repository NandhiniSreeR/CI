package com.tw.bootcamp.bookshop.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidRoleHandler {
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity handleInvalidRequestParams(Throwable ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "request body has invalid params");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
