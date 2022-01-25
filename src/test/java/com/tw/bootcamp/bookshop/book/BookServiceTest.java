package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        Book book = new BookTestBuilder()
                .withName(bookName)
                .withImageUrl("image.jpg")
                .withSmallImageUrl("imageS.jpg")
                .withBooksCount(10)
                .build();
        book = bookRepository.save(book);

        Book bookDetails = bookService.fetchByBookId(book.getId());

        assertEquals("Eclipse (Twilight, #3)", bookDetails.getName());
        assertEquals("J K Rowling", bookDetails.getAuthorName());
        assertEquals("image.jpg", bookDetails.getImageUrl());
        assertEquals("imageS.jpg", bookDetails.getSmallImageUrl());
        assertTrue(bookDetails.isAvailable());
        assertEquals(300, bookDetails.getAmount());

    }

    @Test
    void shouldReturnBookIsAvailableAsTrueWhenBookIsInInventory() throws BookNotFoundException {
        String bookName = "Eclipse (Twilight, #3)";
        int booksCount = 10;
        Book book = new BookTestBuilder().withName(bookName).withBooksCount(booksCount).build();
        book = bookRepository.save(book);

        Book bookDetails = bookService.fetchByBookId(book.getId());

        assertTrue(bookDetails.isAvailable());
    }

    @Test
    void shouldReturnBookIsAvailableAsFalseWhenBookIsNotInInventory() throws BookNotFoundException {
        String bookName = "Eclipse (Twilight, #3)";
        int booksCount = 0;
        Book book = new BookTestBuilder().withName(bookName).withBooksCount(booksCount).build();
        book = bookRepository.save(book);

        Book bookDetails = bookService.fetchByBookId(book.getId());

        assertFalse(bookDetails.isAvailable());
    }

    @Test
    void shouldRespondBookDetailsNotFoundWhenBookIdIsInValid() throws BookNotFoundException {
        Long INVALID_BOOK_ID = 0L;

        BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class,
                () -> bookService.fetchByBookId(INVALID_BOOK_ID));
        assertEquals("Book details not found found for the book id", bookNotFoundException.getMessage());
    }

    @Test
    void shouldPersistBooksGivenInCSV() {
        List<Book> books = new ArrayList<>();
        Book book = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(500D)
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

        bookService.loadBooks(books);
        assertNotNull(bookRepository.findByIsbn("isbn"));
    }

    @Test
    void shouldUpdateBookCountWhenUploadingBooksWithSameIsbn13() {
        List<Book> initialBooks = new ArrayList<>();
        Book book = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(500D)
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

        bookService.loadBooks(initialBooks);

        List<Book> updatedBooks = new ArrayList<>();
        Book updatedBook = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(500D)
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

        bookService.loadBooks(updatedBooks);

        assertEquals(20, bookRepository.findByIsbn13("harrypotter1").getBooksCount());
    }

    @Test
    void shouldUpdateBookCountWhenUploadingBooksAndIsbn13isMissingAndHasSameIsbn() {
        List<Book> initialBooks = new ArrayList<>();
        Book book = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(500D)
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

        bookService.loadBooks(initialBooks);

        List<Book> updatedBooks = new ArrayList<>();
        Book updatedBook = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(500D)
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

        bookService.loadBooks(updatedBooks);

        assertEquals(20, bookRepository.findByIsbn("harrypotter1").getBooksCount());
    }

    @Test
    void shouldUpdateAllFieldsWhenUploadingBooksWithSameIsbn13() {
        List<Book> initialBooks = new ArrayList<>();
        Book book = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(500D)
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

        bookService.loadBooks(initialBooks);

        List<Book> updatedBooks = new ArrayList<>();
        Book updatedBook = Book.builder()
                .name("Harry_Potter")
                .authorName("J.K.Rowling")
                .amount(600D)
                .booksCount(25)
                .averageRating(4.7)
                .currency("INR")
                .imageUrl("imageUrl1")
                .smallImageUrl("smallImageUrl1")
                .isbn("isbn")
                .isbn13("harrypotter1")
                .originalPublicationYear("2014")
                .originalTitle("Harry Potter Part 2")
                .languageCode("LTN")
                .build();
        updatedBooks.add(updatedBook);

        bookService.loadBooks(updatedBooks);

        Book persistedBook = bookRepository.findByIsbn13("harrypotter1");

        assertEquals(updatedBook.getName(), persistedBook.getName());
        assertEquals(updatedBook.getAuthorName(), persistedBook.getAuthorName());
        assertEquals(updatedBook.getBooksCount() + book.getBooksCount(), persistedBook.getBooksCount());
        assertEquals(updatedBook.getAmount(), persistedBook.getAmount());
        assertEquals(updatedBook.getImageUrl(), persistedBook.getImageUrl());
        assertEquals(updatedBook.getSmallImageUrl(), persistedBook.getSmallImageUrl());
        assertEquals(updatedBook.getAverageRating(), persistedBook.getAverageRating());
        assertEquals(updatedBook.getOriginalPublicationYear(), persistedBook.getOriginalPublicationYear());
        assertEquals(updatedBook.getOriginalTitle(), persistedBook.getOriginalTitle());
        assertEquals(updatedBook.getLanguageCode(), persistedBook.getLanguageCode());
    }

    @Test
    void shouldNotPersistWhenNameIsBlank() {
        List<Book> books = new ArrayList<>();
        Book book = Book.builder()
                .name("")
                .authorName("J K Rowling")
                .amount(500D)
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
        books.add(book);

        bookService.loadBooks(books);
        assertNull(bookRepository.findByIsbn13("harrypotter1"));
    }

    @Test
    void shouldNotPersistWhenAuthorNameIsBlank() {
        List<Book> books = new ArrayList<>();
        Book book = Book.builder()
                .name("Harry Potter")
                .authorName("")
                .amount(500D)
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
        books.add(book);

        bookService.loadBooks(books);
        assertNull(bookRepository.findByIsbn13("harrypotter1"));
    }

    @Test
    void shouldNotPersistWhenPriceIsBlank() {
        List<Book> books = new ArrayList<>();
        Book book = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(null)
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
        books.add(book);

        bookService.loadBooks(books);
        assertNull(bookRepository.findByIsbn13("harrypotter1"));
    }

    @Test
    void shouldNotPersistWhenBookCountIsBlank() {
        List<Book> books = new ArrayList<>();
        Book book = Book.builder()
                .name("Harry Potter")
                .authorName("J K Rowling")
                .amount(600D)
                .booksCount(null)
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
        books.add(book);

        bookService.loadBooks(books);
        assertNull(bookRepository.findByIsbn13("harrypotter1"));
    }

}