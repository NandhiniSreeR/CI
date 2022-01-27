package com.tw.bootcamp.bookshop.user.order;

import com.tw.bootcamp.bookshop.book.error.RequiredBookQuantityNotAvailableException;
import com.tw.bootcamp.bookshop.user.order.error.OrderQuantityCannotBeZeroException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order create(Order order) throws RequiredBookQuantityNotAvailableException, OrderQuantityCannotBeZeroException {
        if(order.getQuantity() == 0){
            throw new OrderQuantityCannotBeZeroException();
        }
        if(order.getQuantity() > order.getBookToPurchase().getBooksCount()){
            throw new RequiredBookQuantityNotAvailableException();
        }
        order.getBookToPurchase().decreaseBookCountByQuantity(order.getQuantity());
        return orderRepository.save(order);
    }

}
