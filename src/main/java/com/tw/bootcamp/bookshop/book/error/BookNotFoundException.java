package com.tw.bootcamp.bookshop.book.error;

public class BookNotFoundException extends Exception {
    public BookNotFoundException() {
        super("Book details not found for the book id");
    }
}
