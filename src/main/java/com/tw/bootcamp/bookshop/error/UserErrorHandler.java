package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.payment.exception.InvalidCreditCardDetailsException;
import com.tw.bootcamp.bookshop.user.InvalidEmailException;
import com.tw.bootcamp.bookshop.user.InvalidEmailPatternException;
import com.tw.bootcamp.bookshop.user.InvalidPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserErrorHandler {
    @ExceptionHandler({ InvalidEmailException.class, EmailDoesNotExistException.class, InvalidCreditCardDetailsException.class, InvalidEmailPatternException.class, InvalidPasswordException.class})
    public ResponseEntity<ErrorResponse> handleCreateUserError(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
