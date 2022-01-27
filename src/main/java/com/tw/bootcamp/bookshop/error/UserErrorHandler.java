package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.book.error.RequiredBookQuantityNotAvailableException;
import com.tw.bootcamp.bookshop.payment.exception.InvalidCreditCardDetailsException;
import com.tw.bootcamp.bookshop.user.InvalidEmailException;
import com.tw.bootcamp.bookshop.user.InvalidEmailPatternException;
import com.tw.bootcamp.bookshop.user.InvalidPasswordPatternException;
import com.tw.bootcamp.bookshop.user.PasswordEmptyException;
import com.tw.bootcamp.bookshop.user.order.error.OrderQuantityCannotBeZeroException;
import com.tw.bootcamp.bookshop.user.InvalidPasswordException;
import com.tw.bootcamp.bookshop.user.order.error.OrderQuantityCannotBeLessThanOneException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserErrorHandler {
    @ExceptionHandler({InvalidEmailException.class, EmailDoesNotExistException.class, InvalidEmailPatternException.class,
            PasswordEmptyException.class, InvalidPasswordPatternException.class,
            InvalidCreditCardDetailsException.class, OrderQuantityCannotBeLessThanOneException.class, RequiredBookQuantityNotAvailableException.class})
    public ResponseEntity<ErrorResponse> handleCreateUserError(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
