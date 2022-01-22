package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookRepository;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserRepository;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AddressRepository addressRepository;

    @Autowired public OrderService(OrderRepository orderRepository) {this.orderRepository = orderRepository;}

    public void placeOrder(List<Order> orders) {
        List<Order> newOrders = new ArrayList<>();
        for (Order order : orders) {
            Optional<User> user = userRepository.findById(order.getUser().getId());
            Optional<Address> address = addressRepository.findById(order.getAddress().getId());
            List<Long> bookIds = new ArrayList<>();
            for (Book book : order.getBook()) {
                bookIds.add(book.getId());
            }
            List<Book> books = bookRepository.findByIdIn(bookIds);
            if (user.isPresent() && address.isPresent()) {
                newOrders.add(new Order(user.get(), address.get(), books));
            }
        }
        orderRepository.saveAll(newOrders);
    }

    public List<Order> orders(String email) {
        return orderRepository.findByUserEmail(email);
    }
}
