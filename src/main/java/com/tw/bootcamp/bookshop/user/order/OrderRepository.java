package com.tw.bootcamp.bookshop.user.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

//    List<Order> findAllByOrderDateAfter(Date startDate);

    List<Order> findAllByOrderDateBefore(Date endDate);

    List<Order> findAllByOrderDateBetween(Date startDate, Date endDate);

}
