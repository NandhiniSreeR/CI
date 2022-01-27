package com.tw.bootcamp.bookshop.book.error;

public class RequiredBookQuantityNotAvailableException extends Exception {
    public RequiredBookQuantityNotAvailableException() {
        super("Required book quantity is not available in the system");
    }
}
