package com.tw.bootcamp.bookshop.user.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Objects;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "numeric")
    private int quantity;

    @CreationTimestamp
    @Column(columnDefinition = "timestamp")
    private Date orderDate;

    @NotBlank
    private String paymentMode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address shippingAddress;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book bookToPurchase;

    public Order(int quantity, String paymentMode, User user, Address shippingAddress, Book bookToPurchase){
        this.quantity = quantity;
        this.paymentMode = paymentMode;
        this.user = user;
        this.shippingAddress = shippingAddress;
        this.bookToPurchase = bookToPurchase;
    }

    public static Order create(CreateOrderRequest createRequest, User user, Address address, Book book) {
        return new Order(createRequest.getQuantity(),
                createRequest.getPaymentMode().toString(),
                user,
                address,
                book);
    }

    public OrderResponse toResponse() {
        return OrderResponse.builder()
                .id(id)
                .orderDate(orderDate)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return id != null && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
