package com.tw.bootcamp.bookshop.error;


public class AddressNotValidException extends RuntimeException {

    public AddressErrorResponse addressErrorResponse;
    public AddressNotValidException(AddressErrorResponse addressErrorResponse) {
        this.addressErrorResponse = addressErrorResponse;
    }
}
