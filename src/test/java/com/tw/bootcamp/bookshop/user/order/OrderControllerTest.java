package com.tw.bootcamp.bookshop.user.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.book.BookTestBuilder;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.UserTestBuilder;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import com.tw.bootcamp.bookshop.user.address.AddressTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@WithMockUser
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    UserService userService;

    @MockBean
    AddressService addressService;

    @MockBean
    BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateOrderForValidAddressAndBookId() throws Exception {
        CreateOrderRequest createRequest = createOrder();

        when(userService.findByEmail(anyString())).thenReturn(Optional.of(new UserTestBuilder().build()));
        when(addressService.loadAddressById(anyLong())).thenReturn(Optional.of(new AddressTestBuilder().build()));
        when(bookService.fetchByBookId(anyLong())).thenReturn(new BookTestBuilder().build());
        Order order = Order.builder()
                .id(111L)
                .quantity(1)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY.toString())
                .build();
        when(orderService.create(any(Order.class))).thenReturn(order);
        mockMvc.perform(post("/orders")
                        .content(objectMapper.writeValueAsString(createRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(111L));
    }

    //TODO : Order quantity - negative
    //TODO : Address id does not belong to customer id : currently accepting, should not
    //TODO : Address id does not exist in system : throwing exception instead of error

    private CreateOrderRequest createOrder() {
        return CreateOrderRequest.builder()
                .quantity(1)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY)
                .addressId(3L)
                .bookId(1L)
                .build();
    }
}