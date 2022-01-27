package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.book.error.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Book fetchByBookId(Long id) throws BookNotFoundException {
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()){
            Book bookDetails = book.get();
            bookDetails.isAvailable(bookDetails.getBooksCount() > 0);
            return bookDetails;
        }
        throw new BookNotFoundException();
    }

    public List<BookInformation> loadBooks(List<BookInformation> books) {
        List<BookInformation> failedBooks = new ArrayList<>();
        books.forEach(book -> {
            try {
                Book existingBook = getExistingBook(book);
                if (existingBook == null) {
                    boolean invalidIsbn = book.getIsbn().isEmpty() && book.getIsbn13().isEmpty();
                    if (invalidIsbn) {
                        throw new InvalidBookException();
                    }
                    Book newBook = Book.from(book);
                    bookRepository.save(newBook);
                } else {
                    existingBook.update(book);
                    bookRepository.save(existingBook);
                }
            } catch (Exception e) {
                failedBooks.add(book);
            }
        });
        return failedBooks;
    }

    public Book getExistingBook(BookInformation book) {
        if(!book.getIsbn13().isEmpty()) {
            return bookRepository.findByIsbn13(book.getIsbn13());
        }
        return bookRepository.findByIsbn(book.getIsbn());
    }

    public List<Book> fetchBooksByTitle(String searchString) {
        return bookRepository.findByNameContainsIgnoreCaseOrderByNameAsc(searchString);
    }
}
