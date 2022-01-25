package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDetailsResponse {
    private Long id;
    private String name;
    private String authorName;
    private Money price;
    private String imageUrl;
    private String smallImageUrl;
    private boolean isAvailable;
}
