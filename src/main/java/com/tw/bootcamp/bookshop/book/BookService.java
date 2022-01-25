package com.tw.bootcamp.bookshop.book;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
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

    public List<Book> csvToBooks(InputStream csvInputStream) {
        Reader reader = new InputStreamReader(csvInputStream);
        return new CsvToBeanBuilder(reader)
                .withType(Book.class)
                .build()
                .parse();
    }

    public List<Book> persistBooks(List<Book> books) {
        List<Book> failedBooks = new ArrayList<>();
        books.forEach(book -> {
            try {
                Book existingBook = getExistingBook(book);
                if (existingBook == null) {
                    bookRepository.save(book);
                } else {
                    existingBook.setBooksCount(book.getBooksCount() + existingBook.getBooksCount());
                    bookRepository.save(existingBook);
                }
            } catch (Exception e) {
                failedBooks.add(book);
            }
        });
        return failedBooks;
    }

    public Book getExistingBook(Book book) {
        Book existingBook = bookRepository.findByIsbn13(book.getIsbn13());
        if (existingBook == null) {
            existingBook = bookRepository.findByIsbn(book.getIsbn());
        }
        return existingBook;
    }
}
