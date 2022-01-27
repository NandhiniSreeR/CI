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
public class BookInformation {
    @CsvBindByName(column = "id")
    private Long id;
    @CsvBindByName(column = "title")
    private String name;
    @CsvBindByName(column = "author")
    private String authorName;
    @CsvBindByName(column = "price")
    private Double amount;
    @CsvBindByName(column = "image_url")
    private String imageUrl;
    @CsvBindByName(column = "small_image_url")
    private String smallImageUrl;
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
    private Double averageRating;
}
