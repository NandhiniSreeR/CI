package com.tw.bootcamp.bookshop.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByName;
import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.order.Order;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CsvBindByName(column = "title")
    private String name;
    @CsvBindByName(column = "author")
    private String authorName;
//    @Embedded
//    private Money price;
    @CsvBindByName(column = "price")
    @Column(columnDefinition = "NUMERIC")
    private double amount;
    private String currency;
    @CsvBindByName(column = "image_url")
    private String image_url;
    @CsvBindByName(column = "small_image_url")
    private String small_image_url;
    @Column(columnDefinition = "NUMERIC")
    @CsvBindByName(column = "books_count")
    private Integer books_count;
    @CsvBindByName(column = "isbn13")
    private String isbn13;
    @CsvBindByName(column = "isbn")
    private String isbn;
    @CsvBindByName(column = "original_publication_year")
    private String original_publication_year;
    @CsvBindByName(column = "original_title")
    private String original_title;
    @CsvBindByName(column = "language_code")
    private String language_code;
    @CsvBindByName(column = "average_rating")
    private String average_rating;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "book")
    @JsonIgnore
    private List<Order> order;

    public BookResponse toResponse() {
        return BookResponse.builder()
                .id(id)
                .name(name)
                .authorName(authorName)
                .price(new Money("INR", amount))
                .image_url(image_url)
                .small_image_url(small_image_url)
                .books_count(books_count)
                .isbn13(isbn13)
                .isbn(isbn)
                .original_publication_year(original_publication_year)
                .original_title(original_title)
                .language_code(language_code)
                .average_rating(average_rating)
                .build();
    }
}
