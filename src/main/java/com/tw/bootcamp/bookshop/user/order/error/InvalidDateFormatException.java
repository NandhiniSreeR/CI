package com.tw.bootcamp.bookshop.user.order.error;

public class InvalidDateFormatException extends RuntimeException {
    public InvalidDateFormatException(String msg) {
        super(msg);
    }
}
