package com.tw.bootcamp.bookshop.book;

public class InvalidBookException extends Exception {
    public InvalidBookException() {
        super("Invalid book");
    }
}
