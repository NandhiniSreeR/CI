package com.tw.bootcamp.bookshop.user.order;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderResponse {
    private Long id;
    @NotBlank
    private Date orderDate;
}
