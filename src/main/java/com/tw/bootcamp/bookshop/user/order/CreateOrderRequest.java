package com.tw.bootcamp.bookshop.user.order;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CreateOrderRequest {
    @NotBlank
    private int quantity;
    @NotBlank
    private PaymentMode paymentMode;
    @NotBlank
    private Long addressId;
    @NotBlank
    private Long bookId;
}
