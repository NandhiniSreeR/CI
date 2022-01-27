package com.tw.bootcamp.bookshop.book;

import com.opencsv.bean.CsvToBeanBuilder;
import com.tw.bootcamp.bookshop.book.error.BookNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/books")
    @Operation(summary = "List all books", description = "To list all the books in the bookshop with a book image. " +
            "The result will be sorted in an ascending order based on the Book title.", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "To list all the books in the bookshop with a book image. " +
                    "The result will be sorted in an ascending order based on the Book title.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))})
    })
    List<BookResponse> list() {
        List<Book> books = bookService.fetchAll();
        return books.stream()
                .map(Book::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/books/search", params = "title")
    @Operation(summary = "Search books by title",
            description = "To list all the books in the bookshop based on the title search. " +
                    "The result will be sorted in an ascending order based on the Book title. " +
                    "The search is Case insensitive on books title.",
            tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "To list all the books in the bookshop based on the title search. " +
                    "The result will be sorted in an ascending order based on the Book title. " +
                    "The search is Case insensitive on books title.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))})
    })
    List<BookResponse> listByTitle(@RequestParam String title) {
        List<Book> books = bookService.fetchBooksByTitle(title);
        return books.stream()
                .map(Book::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/admin/load-books", consumes = "multipart/form-data")
    @Operation(summary = "Load books from CSV file", description = "Loads all valid books from the uploaded CSV. Invalid books are returned as a response. " +
            "Invalid book refers to empty values for title, author_name, price, book_count. " +
            "If both ISBN and ISBN13 are empty, or title, author_name, price, book_count are empty then the book is considered invalid.", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books are loaded in Inventory",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))})
    })
    public ResponseEntity<?> loadBooks(@Parameter(description = "A CSV file with book details") @RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.getContentType() == null || !file.getContentType().equals("text/csv")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<BookInformation> books = csvToBooks(file.getInputStream());
        List<BookInformation> failedBooks = bookService.loadBooks(books);
        return new ResponseEntity<>(failedBooks, HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    @Operation(summary = "Show details of a book", description = "Show details of a book in bookshop based on the unique identifier", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show details of a book in bookshop based on the unique identifier",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDetailsResponse.class))}),
            @ApiResponse(responseCode = "404", content = @Content)
    })
    BookDetailsResponse fetch(@Parameter(description = "Unique identifier of the book", example = "1") @PathVariable Long id) throws BookNotFoundException {
        Book book = bookService.fetchByBookId(id);
        return book.toBookDetailsResponse();
    }

    private List<BookInformation> csvToBooks(InputStream csvInputStream) {
        Reader reader = new InputStreamReader(csvInputStream);
        return new CsvToBeanBuilder(reader)
                .withType(BookInformation.class)
                .build()
                .parse();
    }
}
