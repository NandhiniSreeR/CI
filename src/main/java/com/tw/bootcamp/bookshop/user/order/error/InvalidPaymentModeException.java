package com.tw.bootcamp.bookshop.user.order.error;

public class InvalidPaymentModeException extends Exception {
    public InvalidPaymentModeException() {super("Payment mode is invalid.");
    }
}
