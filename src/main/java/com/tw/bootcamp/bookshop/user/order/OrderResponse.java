package com.tw.bootcamp.bookshop.user.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderResponse {
    @Schema(example = "1", description = "Unique Identifier of the Purchase Order")
    private Long id;
    @NotBlank
    @Schema(example = "2022-01-27T05:04:23.776+00:00", description = "Confirmed Order Date")
    private Date orderDate;
}
