package com.tw.bootcamp.bookshop.user.order.error;

public class AddressNotFoundForCustomerException extends Exception {
    public AddressNotFoundForCustomerException() {super("Address does not exist in the system.");
    }
}
