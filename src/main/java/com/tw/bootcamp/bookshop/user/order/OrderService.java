package com.tw.bootcamp.bookshop.user.order;

import com.tw.bootcamp.bookshop.book.error.RequiredBookQuantityNotAvailableException;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressRepository;
import com.tw.bootcamp.bookshop.user.order.error.AddressNotFoundForCustomerException;
import com.tw.bootcamp.bookshop.user.order.error.InvalidPaymentModeException;
import com.tw.bootcamp.bookshop.user.order.error.OrderQuantityCannotBeLessThanOneException;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public Order create(Order order) throws RequiredBookQuantityNotAvailableException, OrderQuantityCannotBeLessThanOneException, AddressNotFoundForCustomerException, InvalidPaymentModeException {
        validateOrder(order);
        order.getBookToPurchase().decreaseBookCountByQuantity(order.getQuantity());
        return orderRepository.save(order);
    }

    private void validateOrder(Order order) throws InvalidPaymentModeException, OrderQuantityCannotBeLessThanOneException, RequiredBookQuantityNotAvailableException, AddressNotFoundForCustomerException {
        validatePaymentMode(order);
        validateOrderQuantity(order);
        validateAddress(order);
    }

    private void validatePaymentMode(Order order) throws InvalidPaymentModeException {
        if(!EnumUtils.isValidEnumIgnoreCase(PaymentMode.class, order.getPaymentMode())){
            throw new InvalidPaymentModeException();
        }
    }

    private void validateOrderQuantity(Order order) throws OrderQuantityCannotBeLessThanOneException, RequiredBookQuantityNotAvailableException {
        if(order.getQuantity() < 1){
            throw new OrderQuantityCannotBeLessThanOneException();
        }
        if(order.getQuantity() > order.getBookToPurchase().getBooksCount()){
            throw new RequiredBookQuantityNotAvailableException();
        }
    }

    private void validateAddress(Order order) throws AddressNotFoundForCustomerException {
        List<Address> addresses = addressRepository.findAllByUser(order.getUser());
        addresses.stream()
                .filter(p -> p.getId() == order.getShippingAddress().getId())
                .findFirst()
                .orElseThrow(AddressNotFoundForCustomerException::new);
    }

    public List<Order> findAllOrdersForAdmin() {
        return orderRepository.findAll();
    }
}
