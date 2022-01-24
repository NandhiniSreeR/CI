package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@WithMockUser
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    UserService userService;

    @Test
    void shouldListAllBooksWhenPresent() throws Exception {
        List<Book> books = new ArrayList<>();
        Book book = new BookTestBuilder().build();
        books.add(book);
        when(bookService.fetchAll()).thenReturn(books);

        mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(bookService, times(1)).fetchAll();
    }

    @Test
    void shouldBeEmptyListWhenNoBooksPresent() throws Exception {
        when(bookService.fetchAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        verify(bookService, times(1)).fetchAll();
    }

    @Test
    void shouldReturnStatusOkWhenCSVFileIsUploaded() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book List.csv");
        MockMultipartFile file = new MockMultipartFile("file", "Book List.csv", "text/csv", uploadStream);

        this.mockMvc.perform(multipart("/admin/load-books")
                .file(file))
                .andExpect(status().isOk());
        if (uploadStream != null) { uploadStream.close(); }
    }

    @Test
    void shouldReturnStatusBadRequestWhenTextFileIsUploaded() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book List.txt");
        MockMultipartFile file = new MockMultipartFile("file", "Book List.txt", "text/plain", uploadStream);

        this.mockMvc.perform(multipart("/admin/load-books")
                .file(file))
                .andExpect(status().isBadRequest());
        if (uploadStream != null) { uploadStream.close(); }
    }

    @Test
    void shouldReturnBadRequestWhenFileIsMissing() throws Exception {
        this.mockMvc.perform(post("/admin/load-books"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCallLoadBooksMethodWhenValidCSVFileIsUploaded() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book List.csv");
        MockMultipartFile file = new MockMultipartFile("file", "Book List.csv", "text/csv", uploadStream);
        when(bookService.loadBooks(any())).thenReturn(true);

        this.mockMvc.perform(multipart("/admin/load-books")
                .file(file))
                .andExpect(status().isOk());

        verify(bookService, times(1)).loadBooks(any());
        if (uploadStream != null) { uploadStream.close(); }
    }


    @Test
    void shouldReturnBookDetailsWhenBookIdIsValid() throws Exception {
        Book book = new BookTestBuilder().withId(56L).build();

        when(bookService.fetchByBookId(56L)).thenReturn(book);

        mockMvc.perform(get("/book/56")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry Potter"));
        verify(bookService, times(1)).fetchByBookId(56L);
    }

    @Test
    void shouldRespondBookDetailsNotFoundWhenBookIdIsInValid() throws Exception {
        long INVALID_BOOK_ID = 0L;
        when(bookService.fetchByBookId(INVALID_BOOK_ID)).thenThrow(new BookNotFoundException());

        mockMvc.perform(get("/book/" + INVALID_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").
                        value("Book details not found found for the book id"));
        verify(bookService, times(1)).fetchByBookId(INVALID_BOOK_ID);
    }
}