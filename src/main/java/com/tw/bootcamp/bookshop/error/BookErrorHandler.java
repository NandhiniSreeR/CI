package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.book.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookErrorHandler {
    @ExceptionHandler({BookNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleBookNotFoundError(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
