package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Long id;
    private String name;
    private String authorName;
    private Money price;
    private String image_url;
    private String small_image_url;
    private Integer books_count;
    private String isbn13;
    private String isbn;
    private String original_publication_year;
    private String original_title;
    private String language_code;
    private String average_rating;
}
