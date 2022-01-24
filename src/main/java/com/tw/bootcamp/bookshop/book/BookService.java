package com.tw.bootcamp.bookshop.book;

import com.opencsv.bean.CsvToBeanBuilder;
import com.tw.bootcamp.bookshop.money.Country;
import com.tw.bootcamp.bookshop.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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

    List<Book> csvToBooks(InputStream stream) {
        Reader reader = new InputStreamReader(stream);
        return new CsvToBeanBuilder(reader)
                .withType(Book.class)
                .build()
                .parse();
    }

    public boolean loadBooks(InputStream stream) throws BookFormatException {
        List<Book> books = csvToBooks(stream);
        List<Book> insertBooks = new ArrayList<>();
        for (Book book : books) {
            Book updateBook = bookRepository.findByIsbn(book.getIsbn());
            Money.rupees(book.getAmount());
            book.setCurrency(Country.INDIA.currency);
            if (updateBook != null) {
                updateBook(book, updateBook);
            } else {
                insertBooks.add(book);
            }
        }
        try {
            bookRepository.saveAll(insertBooks);
        } catch (Exception e) {
            throw new BookFormatException(e.getMessage());
        }
        return true;
    }

    public void updateBook(Book book, Book updateBook) throws BookFormatException {
        book.setId(updateBook.getId());
        book.setBooksCount(updateBook.getBooksCount() + book.getBooksCount());
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            throw new BookFormatException(e.getMessage());
        }
    }
}
