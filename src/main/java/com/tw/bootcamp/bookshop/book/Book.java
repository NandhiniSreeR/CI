package com.tw.bootcamp.bookshop.book;

import com.opencsv.bean.CsvBindByName;
import com.tw.bootcamp.bookshop.money.Money;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@Getter
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
    @Column(columnDefinition = "NUMERIC")
    @CsvBindByName(column = "average_rating")
    private Double averageRating;
    private boolean isAvailable;

    public BookResponse toResponse() {
        return BookResponse.builder()
                .id(id)
                .name(name)
                .authorName(authorName)
                .price(new Money("INR", amount))
                .imageUrl(imageUrl)
                .smallImageUrl(smallImageUrl)
                .isAvailable(isAvailable)
                .build();
    }

    public void setBooksCount(int booksCount) {
        this.booksCount = booksCount;
    }

    public void isAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
