package com.tw.bootcamp.bookshop.user.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookNotFoundException;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private BookService bookService;

    @PostMapping
    @Operation(summary = "Create a new order for the logged in user",
            description = "Creates an order for user with details such as book details, payment mode, user address and quantity",
            tags = {"Order Service"})
    @ApiResponses(value = {@ApiResponse(responseCode = "201",
            description = "Order created", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = OrderResponse.class))})}
    )
    public ResponseEntity<OrderResponse> create(@RequestBody CreateOrderRequest createRequest, Principal principal) throws BookNotFoundException {
        User user = userService.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Address address = addressService.loadAddressById(createRequest.getAddressId()).orElseThrow(() -> new RuntimeException("Address not found"));
        Book book = bookService.fetchByBookId(createRequest.getBookId());
        Order orderToCreate = Order.create(createRequest, user, address, book);
        Order order = orderService.create(orderToCreate);
        OrderResponse orderResponse = order.toResponse();
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
