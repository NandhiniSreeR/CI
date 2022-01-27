package com.tw.bootcamp.bookshop.payment.exception;

public class InvalidCreditCardDetailsException extends RuntimeException {
    public InvalidCreditCardDetailsException() {
        super("Invalid Credit Card Details");
    }
}
