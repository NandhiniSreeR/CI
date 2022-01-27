package com.tw.bootcamp.bookshop.book;

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
    @NotBlank
    @Schema(example = "Harry Potter", description = "Name of the Book")
    private String name;
    @NotBlank
    @Schema(example = "J.K. Rowling", description = "Author of the Book")
    private String authorName;
    @Column(columnDefinition = "NUMERIC")
    @NotNull
    @Schema(example = "1000", description = "Price of the Book")
    private Double amount;
    private String currency = "INR";
    @Schema(example = "www.image.jpg", description = "Image Url of the Book")
    private String imageUrl;
    @Schema(example = "www.Smallimage.jpg", description = "Small Image Url of the Book")
    private String smallImageUrl;
    @Column(columnDefinition = "NUMERIC")
    @NotNull
    private Integer booksCount;
    private String isbn13;
    private String isbn;
    private String originalPublicationYear;
    private String originalTitle;
    private String languageCode;
    @Column(columnDefinition = "NUMERIC")
    private Double averageRating;
    @Transient
    @Schema(example = "True", description = "Is book Available or not")
    private boolean isAvailable;

    public static Book from(BookInformation book) {
        return Book.builder()
                .authorName(book.getAuthorName())
                .name(book.getName())
                .amount(book.getAmount())
                .currency("INR")
                .booksCount(book.getBooksCount())
                .imageUrl(book.getImageUrl())
                .smallImageUrl(book.getSmallImageUrl())
                .isbn13(book.getIsbn13())
                .isbn(book.getIsbn())
                .originalPublicationYear(book.getOriginalPublicationYear())
                .originalTitle(book.getOriginalTitle())
                .languageCode(builder().languageCode)
                .averageRating(book.getAverageRating())
                .build();
    }

    public BookResponse toResponse() {
        return BookResponse.builder()
                .id(id)
                .name(name)
                .authorName(authorName)
                .price(new Money("INR", amount))
                .imageUrl(imageUrl)
                .smallImageUrl(smallImageUrl)
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

    public void update(BookInformation book) {
        this.authorName = book.getAuthorName();
        this.name = book.getName();
        this.amount = book.getAmount();
        this.booksCount = book.getBooksCount() + this.booksCount;
        this.originalPublicationYear = book.getOriginalPublicationYear();
        this.imageUrl = book.getImageUrl();
        this.smallImageUrl = book.getSmallImageUrl();
        this.languageCode = book.getLanguageCode();
        this.originalTitle = book.getOriginalTitle();
        this.averageRating = book.getAverageRating();
    }

    public void isAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void decreaseBookCountByQuantity(int quantity) {
        this.booksCount -= quantity;
    }
}
