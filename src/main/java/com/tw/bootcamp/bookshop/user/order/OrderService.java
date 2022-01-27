package com.tw.bootcamp.bookshop.user.order;

import com.tw.bootcamp.bookshop.book.error.RequiredBookQuantityNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order create(Order order) throws RequiredBookQuantityNotAvailableException {
        if(order.getQuantity() > order.getBookToPurchase().getBooksCount()){
            throw new RequiredBookQuantityNotAvailableException();
        }
        order.getBookToPurchase().decreaseBookCountByQuantity(order.getQuantity());
        return orderRepository.save(order);
    }

}
