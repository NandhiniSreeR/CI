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
    void shouldSaveBookDetailsWhenUploadNonEmptyCSV() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book - Correct Format.csv");
        MockMultipartFile file = new MockMultipartFile("file", "Book - Correct Format.csv", "text/csv", uploadStream);
        assert uploadStream != null;

        this.mockMvc.perform(multipart("/load-books")
                        .file(file))
                .andExpect(status().isOk());

    }

    @Test
    void shouldGiveErrorMessageWhenUploadEmptyFile() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("");
        MockMultipartFile file = new MockMultipartFile("file", uploadStream);
        assert uploadStream != null;

        this.mockMvc.perform(multipart("/load-books")
                        .file(file))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGiveErrorMessageWhenUploadNonCSVFile() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book - Non CSV.txt");
        MockMultipartFile file = new MockMultipartFile("file", "Book - Non CSV.txt", "text/plain", uploadStream);
        assert uploadStream != null;

        this.mockMvc.perform(multipart("/load-books")
                        .file(file))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotUploadCSVWhenHeaderFormatIsIncorrect() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book - Incorrect Headers.csv");
        MockMultipartFile file = new MockMultipartFile("file", "Book - Incorrect Headers.csv", "text/csv", uploadStream);
        when(bookService.loadBooks(any(InputStream.class))).thenThrow(new BookFormatException("Book error"));

        this.mockMvc.perform(multipart("/load-books")
                        .file(file))
                .andExpect(status().isBadRequest());
    }

}