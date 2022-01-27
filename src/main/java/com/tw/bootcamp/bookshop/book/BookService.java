package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.book.error.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            if (bookDetails.getBooksCount() > 0) {
                bookDetails.isAvailable(true);
            } else {
                bookDetails.isAvailable(false);
            }
            return bookDetails;
        }
        throw new BookNotFoundException();
    }

    public void loadBooks(List<Book> books) {
        books.forEach(book -> {
            try {
                Book existingBook = getExistingBook(book);
                if (existingBook == null) {
                    boolean validIsbn = !(book.getIsbn().isEmpty() && book.getIsbn13().isEmpty());
                    if (validIsbn) {
                        bookRepository.save(book);
                    }
                } else {
                    existingBook.update(book);
                    bookRepository.save(existingBook);
                }
            } catch (Exception ignored) {
            }
        });
    }

    public Book getExistingBook(Book book) {
        Book existingBook = bookRepository.findByIsbn13(book.getIsbn13());
        if (existingBook == null) {
            existingBook = bookRepository.findByIsbn(book.getIsbn());
        }
        return existingBook;
    }

    public List<Book> fetchBooksByTitle(String searchString) {
        return bookRepository.findByNameContainsIgnoreCaseOrderByNameAsc(searchString);
    }
}
