package com.tw.bootcamp.bookshop.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AddressNotValidExceptionHandler {

    @ExceptionHandler(AddressNotValidException.class)
    public ResponseEntity<?> handleAll(AddressNotValidException addressNotValidException){
        return new ResponseEntity<>(addressNotValidException.addressErrorResponse,HttpStatus.BAD_REQUEST);
    }

}