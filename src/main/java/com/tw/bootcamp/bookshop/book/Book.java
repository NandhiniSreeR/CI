package com.tw.bootcamp.bookshop.book;

import com.opencsv.bean.CsvBindByName;
import com.tw.bootcamp.bookshop.money.Money;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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
    @NotBlank
    @CsvBindByName(column = "title")
    private String name;
    @NotBlank
    @CsvBindByName(column = "author")
    private String authorName;
//    @Embedded
//    private Money price;
    @CsvBindByName(column = "price")
    @Column(columnDefinition = "NUMERIC")
    private double amount;
    private String currency;
    @CsvBindByName(column = "image_url")
    private String imageUrl;
    @CsvBindByName(column = "small_image_url")
    private String smallImageUrl;
    @Column(columnDefinition = "NUMERIC")
    @CsvBindByName(column = "books_count")
    private Integer booksCount;
    @CsvBindByName(column = "isbn13")
    private String isbn13;
    @CsvBindByName(column = "isbn")
    private String isbn;
    @CsvBindByName(column = "original_publication_year")
    private String originalPublicationYear;
    @CsvBindByName(column = "original_title")
    private String originalTitle;
    @CsvBindByName(column = "language_code")
    private String languageCode;
    @CsvBindByName(column = "average_rating")
    private String averageRating;

    public BookResponse toResponse() {
        return BookResponse.builder()
                .id(id)
                .name(name)
                .authorName(authorName)
                .price(new Money("INR", amount))
                .image_url(imageUrl)
                .small_image_url(smallImageUrl)
                .books_count(booksCount)
                .isbn13(isbn13)
                .isbn(isbn)
                .original_publication_year(originalPublicationYear)
                .original_title(originalTitle)
                .language_code(languageCode)
                .average_rating(averageRating)
                .build();
    }
}
