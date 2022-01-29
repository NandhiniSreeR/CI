package com.tw.bootcamp.bookshop.user.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.book.error.BookNotFoundException;
import com.tw.bootcamp.bookshop.book.error.RequiredBookQuantityNotAvailableException;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import com.tw.bootcamp.bookshop.user.order.error.AddressNotFoundForCustomerException;
import com.tw.bootcamp.bookshop.user.order.error.InvalidDateFormatException;
import com.tw.bootcamp.bookshop.user.order.error.InvalidPaymentModeException;
import com.tw.bootcamp.bookshop.user.order.error.OrderQuantityCannotBeLessThanOneException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class OrderController {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private BookService bookService;

    @PostMapping("/orders")
    @Operation(summary = "Create a new order for the logged in user",
            description = "Creates an order for user with details such as book details, payment mode, user address and quantity",
            tags = {"Order Service"})
    @ApiResponses(value = {@ApiResponse(responseCode = "201",
            description = "Order created", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = OrderResponse.class))})}
    )
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest createRequest, Principal principal) throws BookNotFoundException, RequiredBookQuantityNotAvailableException, OrderQuantityCannotBeLessThanOneException, AddressNotFoundForCustomerException, InvalidPaymentModeException {
        User user = userService.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Address address = addressService.loadAddressById(createRequest.getAddressId()).orElseThrow(() -> new AddressNotFoundForCustomerException());
        Book book = bookService.fetchByBookId(createRequest.getBookId());
        Order orderToCreate = Order.create(createRequest, user, address, book);
        Order order = orderService.create(orderToCreate);
        OrderResponse orderResponse = order.toResponse();
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping("/admin/orders")
    @Operation(summary = "Gets list of all placed orders for Admin only",
            description = "List all orders in the system, restricted to admin role only",
            tags = {"Order Service"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Orders returned", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = AdminOrderResponse.class))})}
    )
    public ResponseEntity<List<AdminOrderResponse>> findAllOrders(
            @RequestParam(required = false, name = "startDate") Optional<String> maybeStartDateStr,
            @RequestParam(required = false, name = "endDate") Optional<String> maybeEndDateStr) {

        List<Order> orders = orderService.findAllOrdersForAdmin(parse(maybeStartDateStr), parse(maybeEndDateStr));

        List<AdminOrderResponse> adminOrderResponses = orders
                .stream()
                .map(Order::toAdminOrderResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(adminOrderResponses, HttpStatus.OK);
    }

    private Optional<Date> parse(Optional<String> maybeDate) {
        try {
            return maybeDate.isPresent() ? Optional.of(dateFormat.parse(maybeDate.get())) : Optional.empty();
        } catch (ParseException e) {
            throw new InvalidDateFormatException("Please enter date in format 'yyyy-MM-dd' e.g. '2020-08-10' ");
        }
    }

}
