package com.tw.bootcamp.bookshop.user.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.book.BookTestBuilder;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.UserTestBuilder;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import com.tw.bootcamp.bookshop.user.address.AddressTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        CreateOrderRequest createRequest = createOrderRequest();

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

    //TODO : Invalid payment mode

    private CreateOrderRequest createOrderRequest() {
        return CreateOrderRequest.builder()
                .quantity(1)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY)
                .addressId(3L)
                .bookId(1L)
                .build();
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void shouldReturnListOfAllPlacedOrders() throws Exception {
        Order firstOrder = createOrder(111L);
        Order secondOrder = createOrder(222L);

        when(orderService.findAllOrdersForAdmin()).thenReturn(asList(firstOrder, secondOrder));

        mockMvc.perform(get("/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderNumber").value(firstOrder.getId()))
                .andExpect(jsonPath("$[1].orderNumber").value(secondOrder.getId()));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void shouldReturnEmptyListWhenNoOrdersPlaced() throws Exception {
        when(orderService.findAllOrdersForAdmin()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    private Order createOrder(Long id) {
        User user = User.builder()
                .id(1L)
                .email("test@tw.com")
                .build();
        Address address = Address.builder()
                .id(1L)
                .mobileNumber(1234567890L)
                .lineNoOne("Line1")
                .lineNoTwo("Line2")
                .city("city")
                .state("state")
                .pinCode("453456")
                .country("India")
                .fullName("Some user")
                .user(user)
                .build();
        Book book = Book.builder()
                .id(5L)
                .name("Some book")
                .isbn("Isbn")
                .isbn13("isbn13")
                .amount(456D)
                .build();
        return Order.builder()
                .id(id)
                .user(user)
                .shippingAddress(address)
                .quantity(5)
                .bookToPurchase(book)
                .build();
    }
}