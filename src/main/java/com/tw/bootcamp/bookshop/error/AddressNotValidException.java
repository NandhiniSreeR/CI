package com.tw.bootcamp.bookshop.error;


public class AddressNotValidException extends RuntimeException {

    AddressErrorResponse addressErrorResponse;
    public AddressNotValidException(AddressErrorResponse addressErrorResponse) {
        this.addressErrorResponse = addressErrorResponse;
    }
}
