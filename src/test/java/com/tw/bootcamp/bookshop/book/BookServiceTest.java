package com.tw.bootcamp.bookshop.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BookServiceTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldFetchAllBooks() {
        Book book = new BookTestBuilder().withName("title").build();
        bookRepository.save(book);

        List<Book> books = bookService.fetchAll();

        assertEquals(1, books.size());
        assertEquals("title", books.get(0).getName());
    }

    @Test
    void shouldFetchAllBooksBeSortedByNameAscending() {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire").build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.fetchAll();

        assertEquals(2, books.size());
        assertEquals("Animal Farm", books.get(0).getName());
    }

    @Test
    void shouldLoadBooksFromUploadedCSV() throws BookFormatException {
        InputStream csvStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book - Correct Format.csv");
        bookService.loadBooks(csvStream);
        List<Book> books = bookService.fetchAll();
        assertEquals(2, books.size());
    }

    @Test
    void shouldLoadBooksAndUpdateFromUploadedCSV() throws BookFormatException {
        InputStream csvStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book - Correct Format.csv");
        bookService.loadBooks(csvStream);
        bookService.loadBooks(csvStream);
        List<Book> books = bookService.fetchAll();
        assertEquals(2, books.size());
    }

    @Test
    void shouldAddBookCountIfISBNNumberAlreadyExists() throws BookFormatException {
        Book oldBook = new Book();
        oldBook.setId(1L);
        oldBook.setIsbn("9736");
        oldBook.setAuthorName("Tester");
        oldBook.setName("Text Book");
        oldBook.setBooksCount(1);

        Book newBook = new Book();
        newBook.setId(1L);
        newBook.setIsbn("9736");
        newBook.setAuthorName("Tester");
        newBook.setName("Text Book");
        newBook.setBooksCount(2);

        bookService.updateBook(oldBook, newBook);
        List<Book> books = bookRepository.findAll();
        assertEquals(3, books.get(0).getBooksCount());
    }

    @Test
    void shouldNotInsertBooksIfNameIsBlank() {
        Book book = new Book();
        book.setBooksCount(1);
        book.setName("");
        book.setAuthorName("Tester");
        assertThrows(BookFormatException.class, () -> bookService.updateBook(book, book));
    }

    @Test
    void shouldNotAcceptIfAuthorNameIsBlank() {
        Book book = new Book();
        book.setBooksCount(1);
        book.setName("Text Book");
        book.setAuthorName("");
        assertThrows(BookFormatException.class, () -> bookService.updateBook(book, book));
    }

}