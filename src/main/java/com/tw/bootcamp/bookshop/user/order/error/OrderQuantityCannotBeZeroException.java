package com.tw.bootcamp.bookshop.user.order.error;

public class OrderQuantityCannotBeZeroException extends Exception {
    public OrderQuantityCannotBeZeroException() {super("Order quantity cannot be Zero");
    }
}
