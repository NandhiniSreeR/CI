package com.tw.bootcamp.bookshop.user.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.error.RequiredBookQuantityNotAvailableException;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressRepository;
import com.tw.bootcamp.bookshop.user.order.error.AddressNotFoundForCustomerException;
import com.tw.bootcamp.bookshop.user.order.error.InvalidPaymentModeException;
import com.tw.bootcamp.bookshop.user.order.error.OrderQuantityCannotBeLessThanOneException;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private AddressRepository addressRepository;

    @Test
    void createOrderAndVerifyIfInventoryIsReduced() throws RequiredBookQuantityNotAvailableException, OrderQuantityCannotBeLessThanOneException, AddressNotFoundForCustomerException, InvalidPaymentModeException {
        Book purchasedBook = Book.builder()
                .id(2222L)
                .isbn13("book2222isbn13")
                .booksCount(10)
                .build();
        User user = User.builder().build();
        Address address = Address.builder().build();
        Order orderToCreate = Order.builder()
                .id(111L)
                .quantity(2)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY.toString())
                .bookToPurchase(purchasedBook)
                .shippingAddress(address)
                .user(user)
                .build();
        List<Address> addresses = Collections.singletonList(address);
        when(addressRepository.findAllByUser(user)).thenReturn(addresses);
        when(orderRepository.save(orderToCreate)).thenReturn(orderToCreate);

        Order createdOrder = orderService.create(orderToCreate);

        assertEquals(8, createdOrder.getBookToPurchase().getBooksCount());
    }

    @Test
    void shouldThrowErrorWhenCreatingOrderWithQuantityGreaterThanInventoryCount() {
        Book purchasedBook = Book.builder()
                .id(2222L)
                .isbn13("book2222isbn13")
                .booksCount(10)
                .build();
        Order orderToCreate = Order.builder()
                .id(111L)
                .quantity(11)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY.toString())
                .bookToPurchase(purchasedBook)
                .build();

        RequiredBookQuantityNotAvailableException requiredBookQuantityNotAvailableException = assertThrows(RequiredBookQuantityNotAvailableException.class,
                () -> orderService.create(orderToCreate));
        assertEquals("Required book quantity is not available in the system", requiredBookQuantityNotAvailableException.getMessage());
    }

    @Test
    void shouldThrowErrorWhenCreatingOrderWithZeroQuantity() {
        Book purchasedBook = Book.builder()
                .id(2222L)
                .isbn13("book2222isbn13")
                .booksCount(10)
                .build();
        Order orderToCreate = Order.builder()
                .id(111L)
                .quantity(0)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY.toString())
                .bookToPurchase(purchasedBook)
                .build();

        OrderQuantityCannotBeLessThanOneException orderQuantityCannotBeLessThanOneException = assertThrows(OrderQuantityCannotBeLessThanOneException.class,
                () -> orderService.create(orderToCreate));
        assertEquals("Order quantity cannot be less than one", orderQuantityCannotBeLessThanOneException.getMessage());
    }

    @Test
    void shouldThrowErrorWhenCreatingOrderWithNegativeQuantity() {
        Book purchasedBook = Book.builder()
                .id(2222L)
                .isbn13("book2222isbn13")
                .booksCount(10)
                .build();
        Order orderToCreate = Order.builder()
                .id(111L)
                .quantity(-1)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY.toString())
                .bookToPurchase(purchasedBook)
                .build();

        OrderQuantityCannotBeLessThanOneException orderQuantityCannotBeLessThanOneException = assertThrows(OrderQuantityCannotBeLessThanOneException.class,
                () -> orderService.create(orderToCreate));
        assertEquals("Order quantity cannot be less than one", orderQuantityCannotBeLessThanOneException.getMessage());
    }

    @Test
    void shouldThrowErrorWhenAddressIdDoesNotBelongToCustomer() {
        Book purchasedBook = Book.builder()
                .id(2222L)
                .isbn13("book2222isbn13")
                .booksCount(10)
                .build();
        long INVALID_ADDRESS_ID = 0L;
        Address address = Address.builder()
                .id(INVALID_ADDRESS_ID)
                .build();
        Order orderToCreate = Order.builder()
                .id(111L)
                .quantity(2)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY.toString())
                .bookToPurchase(purchasedBook)
                .shippingAddress(address)
                .build();

        AddressNotFoundForCustomerException addressNotFoundForCustomerException = assertThrows(AddressNotFoundForCustomerException.class,
                () -> orderService.create(orderToCreate));
        assertEquals("Address does not exist in the system.", addressNotFoundForCustomerException.getMessage());
    }

    @Test
    void shouldThrowErrorWhenPaymentModeIsInvalid() {
        String INVALID_PAYMENT_MODE = "ZETA_CARD";
        Order orderToCreate = Order.builder()
                .id(111L)
                .quantity(2)
                .paymentMode(INVALID_PAYMENT_MODE)
                .build();

        InvalidPaymentModeException invalidPaymentModeException = assertThrows(InvalidPaymentModeException.class,
                () -> orderService.create(orderToCreate));
        assertEquals("Payment mode is invalid.", invalidPaymentModeException.getMessage());
    }

    @Test
    void shouldReturnAllOrdersWhenNoFilterIsApplied() {
        orderService.findAllOrdersForAdmin(Optional.empty(), Optional.empty());
        Date endDate = DateUtils.truncate(Calendar.getInstance().getTime(), Calendar.SECOND);
        verify(orderRepository).findAllByOrderDateBefore(endDate);
    }

    @Test
    void shouldReturnOrdersBeforeEndDate() {
        Date endDate = Date.from(Instant.now().minus(100, ChronoUnit.DAYS));
        orderService.findAllOrdersForAdmin(Optional.empty(), Optional.of(endDate));
        verify(orderRepository).findAllByOrderDateBefore(endDate);
    }

    @Test
    void shouldReturnOrdersFromStartDate() {
        Date startDate = Date.from(Instant.now().minus(100, ChronoUnit.DAYS));
        Date endDate = DateUtils.truncate(Calendar.getInstance().getTime(), Calendar.SECOND);
        orderService.findAllOrdersForAdmin(Optional.of(startDate), Optional.empty());
        verify(orderRepository).findAllByOrderDateBetween(startDate, endDate);
    }
}