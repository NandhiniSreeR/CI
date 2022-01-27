package com.tw.bootcamp.bookshop.user;

public class InvalidEmailPatternException extends Exception {
    public InvalidEmailPatternException() {
        super("Email should follow the pattern abc@xyz.com");
    }
}
