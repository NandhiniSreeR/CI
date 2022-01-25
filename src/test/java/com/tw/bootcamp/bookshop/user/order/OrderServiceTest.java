package com.tw.bootcamp.bookshop.user.order;

import com.tw.bootcamp.bookshop.book.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrderAndVerifyIfInventoryIsReduced() {
        Book purchasedBook = Book.builder()
                .id(2222L)
                .isbn13("book2222isbn13")
                .booksCount(10)
                .build();
        Order orderToCreate = Order.builder()
                .id(111L)
                .quantity(2)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY.toString())
                .bookToPurchase(purchasedBook)
                .build();
        when(orderRepository.save(orderToCreate)).thenReturn(orderToCreate);

        Order createdOrder = orderService.create(orderToCreate);

        assertEquals(8, createdOrder.getBookToPurchase().getBooksCount());
    }
}