package com.tw.bootcamp.bookshop.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> fetchAll() {
        return bookRepository.findAllByOrderByNameAsc();
    }

    public boolean loadBooks(InputStream stream) {
        return true;
    }

    public Book fetchByBookId(Long id) throws BookNotFoundException {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElseThrow(BookNotFoundException::new);
    }
}
