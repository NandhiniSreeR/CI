package com.tw.bootcamp.bookshop.user;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException() {
        super("Password can't be empty");
    }
}
