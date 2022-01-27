package com.tw.bootcamp.bookshop.user.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CreateOrderRequest {
    @NotBlank
    @Schema(example = "1", description = "Order Quantity")
    private int quantity;
    @NotBlank
    @Schema(example = "CREDIT_CARD", description = "Mode of Payment")
    private PaymentMode paymentMode;
    @NotBlank
    @Schema(example = "1", description = "Unique Identifier of the Address")
    private Long addressId;
    @NotBlank
    @Schema(example = "1", description = "Unique Identifier of the Book")
    private Long bookId;
}
