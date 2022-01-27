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
    @Operation(summary = "List all books", description = "Lists all books in bookshop", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List all books",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))})
    })
    List<BookResponse> list() {
        List<Book> books = bookService.fetchAll();
        return books.stream()
                .map(Book::toResponse)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/books", params = "title")
    @Operation(summary = "Search books by title",
            description = "Case insensitive search on books by title that will be ordered in ascending",
            tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search books by title",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))})
    })
    List<BookResponse> listByTitle(@RequestParam String title) {
        List<Book> books = bookService.fetchBooksByTitle(title);
        return books.stream()
                .map(Book::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/admin/load-books",consumes = "multipart/form-data")
    @Operation(summary = "Load all books from CSV file", description = "Load all books from CSV file in inventory", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books are loaded in Inventory",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))})
    })
    public ResponseEntity<?> loadBooks(@Parameter(description = "A CSV file with book details") @RequestParam ("file") MultipartFile file) throws IOException {
        if (file == null || file.getContentType() == null || !file.getContentType().equals("text/csv")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Book> books = csvToBooks(file.getInputStream());
        bookService.loadBooks(books);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    @Operation(summary = "Show book details", description = "Show details of a book in bookshop", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show book details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDetailsResponse.class))}),
            @ApiResponse(responseCode = "404", content = @Content)
    })
    BookDetailsResponse fetch(@Parameter(description = "Unique identifier of the book" , example = "1") @PathVariable Long id) throws BookNotFoundException {
        Book book = bookService.fetchByBookId(id);
        return book.toBookDetailsResponse();
    }

    private List<Book> csvToBooks(InputStream csvInputStream) {
        Reader reader = new InputStreamReader(csvInputStream);
        return new CsvToBeanBuilder(reader)
                .withType(Book.class)
                .build()
                .parse();
    }
}
