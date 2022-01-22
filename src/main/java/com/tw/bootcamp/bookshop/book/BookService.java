package com.tw.bootcamp.bookshop.book;

import com.opencsv.bean.CsvToBeanBuilder;
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

    public void loadBooks(InputStream stream) {
        List<Book> books = csvToBooks(stream);
        List<Book> insertBooks = new ArrayList<>();
        for (Book book : books) {
            Book updateBook = bookRepository.findByIsbn(book.getIsbn());
            Money.rupees(book.getAmount());
            book.setCurrency("INR");
            if (updateBook != null) {
                book.setId(updateBook.getId());
                book.setBooks_count(updateBook.getBooks_count() + book.getBooks_count());
                bookRepository.save(book);
            } else {
                insertBooks.add(book);
            }
        }
        bookRepository.saveAll(insertBooks);
    }
}
