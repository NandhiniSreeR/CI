package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.book.error.RequiredBookQuantityNotAvailableException;
import com.tw.bootcamp.bookshop.user.order.error.AddressNotFoundForCustomerException;
import com.tw.bootcamp.bookshop.user.order.error.InvalidDateFormatException;
import com.tw.bootcamp.bookshop.user.order.error.OrderQuantityCannotBeLessThanOneException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OrderErrorHandler {
    @ExceptionHandler({AddressNotFoundForCustomerException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({RequiredBookQuantityNotAvailableException.class})
    public ResponseEntity<ErrorResponse> handleExpectationFailed(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({OrderQuantityCannotBeLessThanOneException.class, InvalidDateFormatException.class})
    public ResponseEntity<ErrorResponse> handleUnacceptableEntity(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMissingParamEntity(Exception ex) {
        String missingParamMessage = "Mandatory parameters missing";
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST, missingParamMessage);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
