package com.tw.bootcamp.bookshop.user.order.error;

public class OrderQuantityCannotBeLessThanOneException extends Exception {
    public OrderQuantityCannotBeLessThanOneException() {super("Order quantity cannot be less than one");
    }
}
