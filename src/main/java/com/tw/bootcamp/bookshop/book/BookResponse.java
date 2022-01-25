package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    @Schema(example = "1", description = "Unique Identifier of the Book")
    private Long id;
    @Schema(example = "Harry Potter", description = "Name of the Book")
    private String name;
    @Schema(example = "J.K. Rowling", description = "Author of the Book")
    private String authorName;
    private Money price;
    @Schema(example = "www.image.jpg", description = "Image Url of the Book")
    private String imageUrl;
    @Schema(example = "www.Smallimage.jpg", description = "Small Image Url of the Book")
    private String smallImageUrl;
}
