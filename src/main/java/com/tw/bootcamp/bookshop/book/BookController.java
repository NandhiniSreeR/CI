package com.tw.bootcamp.bookshop.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {
    public static final String INVALID_CSV_FILE = "Please input non empty CSV file";
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
                .map(book -> book.toResponse())
                .collect(Collectors.toList());
    }

    @PostMapping("/load-books")
    public ResponseEntity<?> loadBooks(@RequestParam("file") MultipartFile file) throws IOException, BookFormatException {
        if (file == null || file.getContentType() == null ||
                !StringUtils.getFilenameExtension(file.getOriginalFilename()).equals("csv")) {
            return new ResponseEntity<>(INVALID_CSV_FILE, HttpStatus.BAD_REQUEST);
        }
        bookService.loadBooks(file.getInputStream());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
