package com.tw.bootcamp.bookshop.user;

public class InvalidPasswordPatternException extends Exception {
    public InvalidPasswordPatternException() {
        super("Password should be atleast 8 characters with one one special character and one capital alphabet.");
    }
}
