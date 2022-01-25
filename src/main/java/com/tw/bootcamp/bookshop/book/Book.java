package com.tw.bootcamp.bookshop.book;

import com.opencsv.bean.CsvBindByName;
import com.tw.bootcamp.bookshop.money.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @Schema(example = "1", description = "Unique Identifier of the Book")
    private Long id;
    @CsvBindByName(column = "title")
    @NotBlank
    @Schema(example = "Harry Potter", description = "Name of the Book")
    private String name;
    @CsvBindByName(column = "author")
    @NotBlank
    @Schema(example = "J.K. Rowling", description = "Author of the Book")
    private String authorName;
    @CsvBindByName(column = "price")
    @Column(columnDefinition = "NUMERIC")
    @NotNull
    @Schema(example = "1000", description = "Price of the Book")
    private Double amount;
    private String currency;
    @CsvBindByName(column = "image_url")
    @Schema(example = "www.image.jpg", description = "Image Url of the Book")
    private String imageUrl;
    @CsvBindByName(column = "small_image_url")
    @Schema(example = "www.Smallimage.jpg", description = "Small Image Url of the Book")
    private String smallImageUrl;
    @Column(columnDefinition = "NUMERIC")
    @CsvBindByName(column = "books_count")
    @NotNull
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
    @Transient
    @Schema(example = "True", description = "Is book Available or not")
    private boolean isAvailable;

    public BookResponse toResponse() {
        return BookResponse.builder()
                .id(id)
                .name(name)
                .authorName(authorName)
                .price(new Money("INR", amount))
                .build();
    }

    public BookDetailsResponse toBookDetailsResponse() {
        return BookDetailsResponse.builder()
                .id(id)
                .name(name)
                .authorName(authorName)
                .price(new Money("INR", amount))
                .imageUrl(imageUrl)
                .smallImageUrl(smallImageUrl)
                .isAvailable(isAvailable)
                .build();
    }

    public void update(Book book) {
        this.authorName = book.authorName;
        this.name = book.name;
        this.amount = book.amount;
        this.currency = book.currency;
        this.booksCount = book.booksCount + this.booksCount;
        this.originalPublicationYear = book.originalPublicationYear;
        this.imageUrl = book.imageUrl;
        this.smallImageUrl = book.smallImageUrl;
        this.languageCode = book.languageCode;
        this.originalTitle = book.originalTitle;
        this.averageRating = book.averageRating;
    }

    public void isAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
