package com.tw.bootcamp.bookshop.user.order;

import com.tw.bootcamp.bookshop.user.address.AddressResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class AdminOrderResponse {
    private Long orderNumber;
    private Date orderDate;
    private String customerName;
    private Long phoneNumber;
    private String bookName;
    private String bookIsbn;
    private String bookIsbn13;
    private Double totalCost;
    private Integer noOfCopies;
    private AddressResponse address;
}
