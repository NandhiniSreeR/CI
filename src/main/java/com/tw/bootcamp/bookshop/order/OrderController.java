package com.tw.bootcamp.bookshop.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired public OrderController(OrderService orderService) {this.orderService = orderService;}

    @GetMapping("/orders")
    public List<Order> orders() {
        return orderService.orders("amulya@tw.com");
    }

    @RequestMapping(value = "/place-order", method = RequestMethod.POST)
    public void placeOrder(@RequestBody List<Order> orders) {
        orderService.placeOrder(orders);
    }

}
