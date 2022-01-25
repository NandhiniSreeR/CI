package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void shouldReturnBookDetailsWhenBookIdIsValid() throws BookNotFoundException {
        String bookName = "Eclipse (Twilight, #3)";
        Book book = new BookTestBuilder().
                withName(bookName).
                withImageUrl("image.jpg").
                withSmallImageUrl("imageS.jpg").
                withBooksCount(10).build();
        book = bookRepository.save(book);

        Book bookDetails = bookService.fetchByBookId(book.getId());

        assertEquals("Eclipse (Twilight, #3)", bookDetails.getName());
        assertEquals("J K Rowling", bookDetails.getAuthorName());
        assertEquals("image.jpg", bookDetails.getImageUrl());
        assertEquals("imageS.jpg", bookDetails.getSmallImageUrl());
        assertEquals(true, bookDetails.isAvailable());
        assertEquals(300, bookDetails.getAmount());

    }

    @Test
    void shouldReturnBookIsAvailableAsTrueWhenBookIsInInventory() throws BookNotFoundException {
        String bookName = "Eclipse (Twilight, #3)";
        int booksCount = 10;
        Book book = new BookTestBuilder().withName(bookName).withBooksCount(booksCount).build();
        book = bookRepository.save(book);

        Book bookDetails = bookService.fetchByBookId(book.getId());

        assertEquals(true, bookDetails.isAvailable());
    }

    @Test
    void shouldReturnBookIsAvailableAsFalseWhenBookIsNotInInventory() throws BookNotFoundException {
        String bookName = "Eclipse (Twilight, #3)";
        int booksCount = 0;
        Book book = new BookTestBuilder().withName(bookName).withBooksCount(booksCount).build();
        book = bookRepository.save(book);

        Book bookDetails = bookService.fetchByBookId(book.getId());

        assertEquals(false, bookDetails.isAvailable());
    }

    @Test
    void shouldParseCSVFileWhenCSVIsUploaded() throws IOException {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book List.csv");
        MockMultipartFile file = new MockMultipartFile("file", "Book List.csv", "text/csv", uploadStream);

        List<Book> books = bookService.csvToBooks(file.getInputStream());
        assertEquals(2, books.size());
        assertEquals("City of Jones (The Mortal Instruments, #1)", books.get(0).getName());
        if (uploadStream != null) {
            uploadStream.close();
        }
    }

    @Test
    void shouldPersistBooksGivenInCSV() {
        List<Book> books = new ArrayList<>();
        Book book = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(500)
                .booksCount(5)
                .averageRating(4.5)
                .currency("INR")
                .imageUrl("imageUrl")
                .smallImageUrl("smallImageUrl")
                .isbn("isbn")
                .isbn13("isbn13")
                .originalPublicationYear("2013")
                .originalTitle("Harry Potter Part 1")
                .languageCode("ENG")
                .build();
        books.add(book);

        List<Book> failedBooks = bookService.persistBooks(books);
        assertEquals(0, failedBooks.size());
        assertNotNull(bookRepository.findByIsbn("isbn"));
    }

    @Test
    void shouldUpdateBookCountWhenUploadingBooksWithSameIsbn13() {
        List<Book> initialBooks = new ArrayList<>();
        Book book = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(500)
                .booksCount(5)
                .averageRating(4.5)
                .currency("INR")
                .imageUrl("imageUrl")
                .smallImageUrl("smallImageUrl")
                .isbn("isbn")
                .isbn13("harrypotter1")
                .originalPublicationYear("2013")
                .originalTitle("Harry Potter Part 1")
                .languageCode("ENG")
                .build();
        initialBooks.add(book);

        bookService.persistBooks(initialBooks);

        List<Book> updatedBooks = new ArrayList<>();
        Book updatedBook = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(500)
                .booksCount(15)
                .averageRating(4.5)
                .currency("INR")
                .imageUrl("imageUrl")
                .smallImageUrl("smallImageUrl")
                .isbn("isbn")
                .isbn13("harrypotter1")
                .originalPublicationYear("2013")
                .originalTitle("Harry Potter Part 1")
                .languageCode("ENG")
                .build();
        updatedBooks.add(updatedBook);

        bookService.persistBooks(updatedBooks);

        assertEquals(20, bookRepository.findByIsbn13("harrypotter1").getBooksCount());
    }

    @Test
    void shouldUpdateBookCountWhenUploadingBooksWhenIsbn13isMissingAndHasSameIsbn() {
        List<Book> initialBooks = new ArrayList<>();
        Book book = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(500)
                .booksCount(5)
                .averageRating(4.5)
                .currency("INR")
                .imageUrl("imageUrl")
                .smallImageUrl("smallImageUrl")
                .isbn("harrypotter1")
                .isbn13("")
                .originalPublicationYear("2013")
                .originalTitle("Harry Potter Part 1")
                .languageCode("ENG")
                .build();
        initialBooks.add(book);

        bookService.persistBooks(initialBooks);

        List<Book> updatedBooks = new ArrayList<>();
        Book updatedBook = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(500)
                .booksCount(15)
                .averageRating(4.5)
                .currency("INR")
                .imageUrl("imageUrl")
                .smallImageUrl("smallImageUrl")
                .isbn("harrypotter1")
                .isbn13("harrypotter1")
                .originalPublicationYear("2013")
                .originalTitle("Harry Potter Part 1")
                .languageCode("ENG")
                .build();
        updatedBooks.add(updatedBook);

        bookService.persistBooks(updatedBooks);

        assertEquals(20, bookRepository.findByIsbn("harrypotter1").getBooksCount());
    }

}