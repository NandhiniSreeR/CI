package com.tw.bootcamp.bookshop.error;

public class EmailDoesNotExistException extends RuntimeException {
    public EmailDoesNotExistException() {
        super("User email does not exist");
    }
}
