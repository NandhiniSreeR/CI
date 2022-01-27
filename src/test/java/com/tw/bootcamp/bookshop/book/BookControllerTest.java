package com.tw.bootcamp.bookshop.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.book.error.BookNotFoundException;
import com.tw.bootcamp.bookshop.user.Role;
import com.tw.bootcamp.bookshop.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
    void shouldFetchMatchingBooksOnTitleSearch() throws Exception {
        List<Book> books = new ArrayList<>();
        Book book = new BookTestBuilder().build();
        books.add(book);
        when(bookService.fetchBooksByTitle("Harry")).thenReturn(books);

        mockMvc.perform(get("/books/search")
                        .param("title","Harry")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(bookService, times(1)).fetchBooksByTitle("Harry");
    }

    @Test
    void shouldReturnEmptyListOnTitleSearchWhenNoMatchingBooksFound() throws Exception {
        List<Book> books = new ArrayList<>();
        when(bookService.fetchBooksByTitle("Animal")).thenReturn(books);

        mockMvc.perform(get("/books/search")
                        .param("title","Animal")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        verify(bookService, times(1)).fetchBooksByTitle("Animal");
    }

    @Test
    void shouldReturnStatusOkWhenCSVFileIsUploaded() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book List.csv");
        MockMultipartFile file = new MockMultipartFile("file", "Book List.csv", "text/csv", uploadStream);

        this.mockMvc.perform(multipart("/admin/load-books")
                        .file(file).with(user("admin@bookshopify.com").password("admin").roles(Role.ADMIN.name())))
                .andExpect(status().isOk());
        if (uploadStream != null) {
            uploadStream.close();
        }
    }

    @Test
    void shouldReturnStatusBadRequestWhenTextFileIsUploaded() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book List.txt");
        MockMultipartFile file = new MockMultipartFile("file", "Book List.txt", "text/plain", uploadStream);

        this.mockMvc.perform(multipart("/admin/load-books")
                        .file(file).with(user("admin@bookshopify.com").password("admin").roles(Role.ADMIN.name())))
                .andExpect(status().isBadRequest());
        if (uploadStream != null) {
            uploadStream.close();
        }
    }

    @Test
    void shouldNotUploadWhenNonAdminTriedToUploadTheBooks() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book List.csv");
        MockMultipartFile file = new MockMultipartFile("file", "Book List.csv", "text/csv", uploadStream);

        this.mockMvc.perform(multipart("/admin/load-books")
                        .file(file).with(user("user@bookshopify.com").password("user").roles(Role.USER.name())))
                .andExpect(status().isForbidden());
        if (uploadStream != null) {
            uploadStream.close();
        }
    }

    @Test
    void shouldReturnBadRequestWhenFileIsMissing() throws Exception {
        this.mockMvc.perform(multipart("/admin/load-books").with(user("admin@bookshopify.com").password("admin").roles(Role.ADMIN.name())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCallLoadBooksMethodWhenValidCSVFileIsUploaded() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book List.csv");
        MockMultipartFile file = new MockMultipartFile("file", "Book List.csv", "text/csv", uploadStream);

        Book firstBook = Book.builder()
                .name("City of Jones (The Mortal Instruments, #1)")
                .authorName("Cassandra Clare")
                .amount(1461D)
                .booksCount(178)
                .averageRating(4.12)
                .currency("INR")
                .imageUrl("https://images.gr-assets.com/books/1432730315m/256683.jpg")
                .smallImageUrl("https://images.gr-assets.com/books/1432730315s/256683.jpg")
                .isbn("1416914285")
                .isbn13("9781416914280")
                .originalPublicationYear("2007")
                .originalTitle("City of Bones")
                .languageCode("eng")
                .build();

        this.mockMvc.perform(multipart("/admin/load-books")
                        .file(file).with(user("admin@bookshopify.com").password("admin").roles(Role.ADMIN.name())))
                .andExpect(status().isOk());

        ArgumentCaptor<List<BookInformation>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        then(bookService).should().loadBooks(argumentCaptor.capture());
        List<BookInformation> capturedBooks = argumentCaptor.getValue();
        BookInformation firstCapturedBook = capturedBooks.get(0);

        assertEquals(firstBook.getName(), firstCapturedBook.getName());
        assertEquals(firstBook.getAuthorName(), firstCapturedBook.getAuthorName());
        assertEquals(firstBook.getBooksCount(), firstCapturedBook.getBooksCount());
        assertEquals(firstBook.getAmount(), firstCapturedBook.getAmount());
        assertEquals(firstBook.getImageUrl(), firstCapturedBook.getImageUrl());
        assertEquals(firstBook.getSmallImageUrl(), firstCapturedBook.getSmallImageUrl());
        assertEquals(firstBook.getAverageRating(), firstCapturedBook.getAverageRating());
        assertEquals(firstBook.getOriginalPublicationYear(), firstCapturedBook.getOriginalPublicationYear());
        assertEquals(firstBook.getOriginalTitle(), firstCapturedBook.getOriginalTitle());
        assertEquals(firstBook.getLanguageCode(), firstCapturedBook.getLanguageCode());
        assertEquals(firstBook.getIsbn(), firstCapturedBook.getIsbn());
        assertEquals(firstBook.getIsbn13(), firstCapturedBook.getIsbn13());

        verify(bookService, times(1)).loadBooks(any());
        if (uploadStream != null) {
            uploadStream.close();
        }
    }

    @Test
    void shouldReturnBookDetailsWhenBookIdIsValid() throws Exception {
        long BOOK_ID = 56L;
        Book book = new BookTestBuilder().withId(BOOK_ID).build();

        when(bookService.fetchByBookId(BOOK_ID)).thenReturn(book);

        mockMvc.perform(get("/books/56")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry Potter"));
        verify(bookService, times(1)).fetchByBookId(BOOK_ID);
    }

    @Test
    void shouldRespondBookDetailsNotFoundWhenBookIdIsInValid() throws Exception {
        long INVALID_BOOK_ID = 0L;
        when(bookService.fetchByBookId(INVALID_BOOK_ID)).thenThrow(new BookNotFoundException());

        mockMvc.perform(get("/books/" + INVALID_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").
                        value("Book details not found for the book id"));
        verify(bookService, times(1)).fetchByBookId(INVALID_BOOK_ID);
    }

    @Test
    void shouldReturnFailedBooksWhenUploadedCSVHasInvalidBooks() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Invalid Book List.csv");
        MockMultipartFile file = new MockMultipartFile("file", "Invalid Book List.csv", "text/csv", uploadStream);

        List<BookInformation> failedBooks = new ArrayList<>();
        BookInformation book = BookInformation.builder()
                .id(52L)
                .name("")
                .authorName("Stephenie Meyer")
                .amount(2335D)
                .booksCount(185)
                .averageRating(3.69)
                .imageUrl("https://images.gr-assets.com/books/1361038355m/428263.jpg")
                .smallImageUrl("https://images.gr-assets.com/books/1361038355s/428263.jpg")
                .isbn("316160202")
                .isbn13("harry")
                .originalPublicationYear("2007")
                .originalTitle("Eclipse")
                .languageCode("en-US")
                .build();
        failedBooks.add(book);
        when(bookService.loadBooks(any())).thenReturn(failedBooks);

        MvcResult result = this.mockMvc.perform(multipart("/admin/load-books")
                        .file(file).with(user("admin@bookshopify.com").password("admin").roles(Role.ADMIN.name())))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();

        List<BookInformation> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<BookInformation>>() {});

        assertEquals(book.getId(), actual.get(0).getId());

        if (uploadStream != null) {
            uploadStream.close();
        }
    }

    @Test
    void shouldNotReturnBooksWhenUploadedCSVHasValidBooks() throws Exception {
        InputStream uploadStream = BookControllerTest.class.getClassLoader().getResourceAsStream("Book List.csv");
        MockMultipartFile file = new MockMultipartFile("file", "Book List.csv", "text/csv", uploadStream);

        when(bookService.loadBooks(any())).thenReturn(Collections.emptyList());

        MvcResult result = this.mockMvc.perform(multipart("/admin/load-books")
                        .file(file).with(user("admin@bookshopify.com").password("admin").roles(Role.ADMIN.name())))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();

        List<BookInformation> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<BookInformation>>() {});

        assertEquals(0, actual.size());

        if (uploadStream != null) {
            uploadStream.close();
        }
    }
}