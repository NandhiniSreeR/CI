package com.tw.bootcamp.bookshop.user;

public class PasswordEmptyException extends Exception {
    public PasswordEmptyException() {
        super("Password can't be empty");
    }
}
